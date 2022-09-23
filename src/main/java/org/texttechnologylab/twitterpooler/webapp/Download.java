package org.texttechnologylab.twitterpooler.webapp;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import io.quarkus.mongodb.MongoClientName;
import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.CasIOUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.texttechnologylab.uimadb.UIMADatabaseInterface;
import org.xml.sax.SAXException;
import org.texttechnologylab.twitterpooler.twitter.PoolAccount;
import org.texttechnologylab.twitterpooler.twitter.QueryAccount;
import org.texttechnologylab.twitterpooler.twitter.Tweet;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.apache.uima.cas.SerialFormat.XMI;

@Path("download")
public class Download {

    @Inject
    @MongoClientName("tweets")
    MongoClient mongoClientTweets;

    @Inject
    @MongoClientName("pools")
    MongoClient mongoClientPools;

    @GET
    @Path("get")
    @Produces(MediaType.WILDCARD)
    public Response getFile(@Context UriInfo uriInfo) throws IOException, UIMAException, SAXException {

        String art = uriInfo.getQueryParameters().get("art").get(0);
        File tFile = null;
        if (art.equals("query"))
        {
            //get file from mongodoDB
            String queryId = uriInfo.getQueryParameters().get("queryId").get(0);
            String file = uriInfo.getQueryParameters().get("file").get(0);
            String prefixx = queryId;
            File parent = new File(System.getProperty("java.io.tmpdir"));
            if(file.equals("one"))
            {
                tFile = new File(parent, prefixx + ".xmi");
            }
            else
            {
                tFile = new File(parent, prefixx + ".zip");
            }
            FileOutputStream stream = new FileOutputStream(tFile);
            MongoDatabase db = mongoClientPools.getDatabase("Files");
            GridFSBucket grid = GridFSBuckets.create(db);
            grid.downloadToStream(tFile.getName(),stream);
            stream.close();
        }
        else
        {
            //get file from mongodoDB
            String queryId = uriInfo.getQueryParameters().get("queryId").get(0);
            String file = uriInfo.getQueryParameters().get("file").get(0);
            String prefixx = art+"-"+queryId+"-"+file;
            File parent = new File(System.getProperty("java.io.tmpdir"));
            tFile = new File(parent, prefixx + ".zip");
            FileOutputStream stream = new FileOutputStream(tFile);
            MongoDatabase db = mongoClientPools.getDatabase("Files");
            GridFSBucket grid = GridFSBuckets.create(db);
            grid.downloadToStream(tFile.getName(),stream);
            stream.close();
        }

        Response.ResponseBuilder response = Response.ok((Object) tFile);
        response.header("Content-Disposition", "attachment;filename="+tFile.getName());

        return response.build();
    }

    @GET
    @Path("delete")
    public void deleteFile(@Context UriInfo uriInfo)
    {
        MongoDatabase db = mongoClientPools.getDatabase("Files");
        //get objectId from file name
        MongoCollection coll = db.getCollection("fs.files");
        String file = uriInfo.getQueryParameters().get("filename").get(0);
        String art = uriInfo.getQueryParameters().get("art").get(0);
        String queryId = uriInfo.getQueryParameters().get("queryId").get(0);
        String fileType = uriInfo.getQueryParameters().get("file").get(0);
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("filename", file);
        FindIterable cursor = coll.find(whereQuery);
        Document doc = (Document)cursor.first();
        //var sd = coll.find("filename",);
        GridFSBucket grid = GridFSBuckets.create(db);
        ObjectId fileId = new ObjectId(doc.get("_id").toString());
        grid.delete(fileId);

        //update status
        if(art.equals("query"))
        {
            QueryAccount account = QueryAccount.getQueryAccount(queryId);
            if(fileType.equals("one"))
            {
                account.setOne("no available");
                account.persistOrUpdate();
            }
            else
            {
                account.setMulti("no available");
                account.persistOrUpdate();
            }
        }
        else
        {
            PoolAccount account = PoolAccount.getPoolAccount(queryId,art);
            if(fileType.equals("one"))
            {
                account.setOne("no available");
                account.persistOrUpdate();
            }
            else
            {
                account.setMulti("no available");
                account.persistOrUpdate();
            }
        }


    }

    @GET
    @Path("upload")
    public void uploadFile(@Context UriInfo info)
    {
        final ExecutorService threadPool = Executors.newCachedThreadPool();
        MultivaluedMap<String, String> tempUriInfo = new MultivaluedMapImpl<>();
        tempUriInfo = info.getQueryParameters();
        MultivaluedMap<String, String> finalTempUriInfo = tempUriInfo;

        Runnable r = new Runnable() {
            private MultivaluedMap<String, String> uriInfo = finalTempUriInfo;

            @Override
            public void run() {
                try
                {
                    long startTime = System.currentTimeMillis();
                    File tFile = null;

                    if(uriInfo.get("type").get(0).equals("pool"))
                    {
                        String poolingArt = uriInfo.get("art").get(0);
                        String queryId = uriInfo.get("queryId").get(0);
                        String file = uriInfo.get("file").get(0);

                        PoolAccount account = PoolAccount.getPoolAccount(queryId, poolingArt);


                        ArrayList<String> listOfPools = getPoolsCollections(poolingArt, queryId);
                        if(file.equals("one"))
                        {
                            account.setOne("creating");
                            account.persistOrUpdate();
                            tFile = oneFilePools(listOfPools, queryId, poolingArt, file);
                            account.setOne("available");
                            account.persistOrUpdate();
                        }
                        else
                        {
                            account.setMulti("creating");
                            account.persistOrUpdate();
                            tFile = multiplyFilesPools(listOfPools, queryId, poolingArt, file);
                            account.setMulti("available");
                            account.persistOrUpdate();
                        }


                    }
                    else
                    {
                        String file = uriInfo.get("file").get(0);
                        String queryId = uriInfo.get("queryId").get(0);
                        QueryAccount account = QueryAccount.getQueryAccount(queryId);
                        ArrayList<Tweet> tweets = Tweet.getTweetsByQueryId(queryId,mongoClientTweets);
                        if(file.equals("one"))
                        {
                            account.setOne("creating");
                            account.persistOrUpdate();
                            tFile = xmiTweets(tweets, queryId);
                            account.setOne("available");
                            account.persistOrUpdate();
                            long endTime = System.currentTimeMillis();
                            System.out.println("One took " + (endTime - startTime) + " milliseconds");
                        }
                        else
                        {
                            account.setMulti("creating");
                            account.persistOrUpdate();
                            tFile = zipTweets(tweets, queryId);
                            account.setMulti("available");
                            account.persistOrUpdate();
                            long endTime = System.currentTimeMillis();
                            System.out.println("Multi took " + (endTime - startTime) + " milliseconds");
                        }
                    }
                    //storing File in mongoDB
                    InputStream stream = new FileInputStream(tFile);
                    MongoDatabase db = mongoClientPools.getDatabase("Files");
                    GridFSBucket grid = GridFSBuckets.create(db);
                    ObjectId fileId = grid.uploadFromStream(tFile.getName(),stream);

                }
                catch (Exception e){}
            }
        };
        threadPool.submit(r);
    }
    public File xmiTweets(ArrayList<Tweet> tweets, String queryId) throws IOException, UIMAException, SAXException {
        File parent = new File(System.getProperty("java.io.tmpdir"));
        File tFile = new File(parent, queryId + ".xmi");
        FileOutputStream stream = new FileOutputStream(tFile);

        for (int i = 0; i < tweets.size();i++)
        {
            String cas = tweets.get(i).getCasObject();
            long startTime = System.currentTimeMillis();
            JCas tCas = UIMADatabaseInterface.deserializeJCas(cas);
            //JCas tCas = null;
            //MongoSerialization.deserializeJCas(tCas,cas);
            //ByteArrayInputStream tmpStream = new ByteArrayInputStream(cas.getBytes("UTF-16"));
            //JCas jCas = JCasFactory.createJCas();
            //XCASDeserializer.deserialize(tmpStream, jCas.getCas());
            long endTime = System.currentTimeMillis();
            System.out.println("Deserilizer took " + (endTime - startTime) + " milliseconds");

            long startTime2 = System.currentTimeMillis();
            CasIOUtils.save(tCas.getCas(), stream, XMI);
            long endTime2 = System.currentTimeMillis();
            System.out.println("CasIOUtils took " + (endTime2 - startTime2) + " milliseconds");
        }
        return tFile;
    }

    public File zipTweets(ArrayList<Tweet> tweets, String queryId) throws IOException, UIMAException {

        File parent = new File(System.getProperty("java.io.tmpdir"));
        File tFile = new File(parent, queryId + ".zip");
        FileOutputStream stream = new FileOutputStream(tFile);
        ZipOutputStream out = new ZipOutputStream(stream);

        for (int i = 0; i < tweets.size();i++)
        {
            String cas = tweets.get(i).getCasObject();
            JCas tCas = UIMADatabaseInterface.deserializeJCas(cas);
            File parentTemp = new File(System.getProperty("java.io.tmpdir"));
            File  tempFile = new File(parent, tweets.get(i).getObject_id() + ".xmi");
            FileOutputStream tempOutput = new FileOutputStream(tempFile);
            CasIOUtils.save(tCas.getCas(), tempOutput, XMI);
            FileInputStream input = new FileInputStream(tempFile);

            ZipEntry e = new ZipEntry(tempFile.getName());
            out.putNextEntry(e);
            writeData(input,out);
            //close all
            tempOutput.close();
            input.close();
            tempFile.deleteOnExit();
            out.closeEntry();
        }

        stream.close();
        return tFile;
    }

    public File oneFilePools(ArrayList<String> listOfPoolsNames, String queryId, String poolingArt, String file) throws IOException, UIMAException, SAXException {
        /*
        * Plan
        * - search all pools
        * - loop through pools ()
        * */
        String fileName = poolingArt +"-"+queryId+"-"+file ;
        File parent = new File(System.getProperty("java.io.tmpdir"));
        File tFile = new File(parent, fileName + ".zip");
        FileOutputStream stream = new FileOutputStream(tFile);
        ZipOutputStream out = new ZipOutputStream(stream);

        for(int i = 0; i< listOfPoolsNames.size();i++)
        {
            //create new directory with pool name
            String poolName = listOfPoolsNames.get(i);
            String dir = poolName+"/";
            //get tweets and loop through
            ArrayList<Tweet> tweets = Tweet.getTweetsByPool(poolName, mongoClientTweets);
            File tempFile = xmiTweets(tweets,queryId);
            FileInputStream input = new FileInputStream(tempFile);
            ZipEntry e = new ZipEntry(dir+"Tweets.xmi");
            out.putNextEntry(e);
            writeData(input,out);

            //close all
            input.close();
            tempFile.deleteOnExit();
            out.closeEntry();

        }
        stream.close();
        tFile.deleteOnExit();
        return tFile;
    }

    public File multiplyFilesPools(ArrayList<String> listOfPoolsNames, String queryId, String poolingArt, String file) throws IOException, UIMAException {
        String fileName = poolingArt +"-"+queryId+"-"+file ;
        File parent = new File(System.getProperty("java.io.tmpdir"));
        File  tFile = new File(parent, fileName + ".zip");
        FileOutputStream stream = new FileOutputStream(tFile);
        ZipOutputStream out = new ZipOutputStream(stream);

        for(int i = 0; i< listOfPoolsNames.size();i++)
        {
            //create new directory with pool name
            String poolName = listOfPoolsNames.get(i);
            String dir = poolName+"/";
            //get tweets and loop through
            ArrayList<Tweet> tweets = Tweet.getTweetsByPool(poolName, mongoClientTweets);
            writeConvertedTweets(tweets,out,dir);
        }
        stream.close();
        tFile.deleteOnExit();
        return tFile;
    }

    public ArrayList<String> getPoolsCollections(String poolingArt, String queryId)
    {
        ArrayList<String> listOfPoolsNames = new ArrayList<String>();
        MongoDatabase mongoClient = mongoClientPools.getDatabase("Pools");
        MongoIterable<String> list = mongoClient.listCollectionNames();
        for (String name : list)
        {
            if(name.contains(poolingArt) && name.contains(queryId))
            {
                listOfPoolsNames.add(name);
            }
        }
        return listOfPoolsNames;
    }

    public void writeData(FileInputStream input, ZipOutputStream out) throws IOException {
        byte[] bytes = new byte[1024];
        int length;
        while((length = input.read(bytes)) >= 0) {
            out.write(bytes, 0, length);
        }
    }


    public void writeConvertedTweets(ArrayList<Tweet> tweets, ZipOutputStream out, String dir) throws UIMAException, IOException {
        for (int i = 0; i < tweets.size();i++)
        {
            String cas = tweets.get(i).getCasObject();
            JCas tCas = UIMADatabaseInterface.deserializeJCas(cas);
            File tempFile = File.createTempFile(tweets.get(i).getObject_id(),".xmi", null);
            System.out.println("Created "+tempFile.getName());
            FileOutputStream tempOutput = new FileOutputStream(tempFile);
            CasIOUtils.save(tCas.getCas(), tempOutput, XMI);
            FileInputStream input = new FileInputStream(tempFile);

            if (dir != null)
            {
                ZipEntry e = new ZipEntry(dir+tempFile.getName());
                out.putNextEntry(e);
            }
            else
            {
                ZipEntry e = new ZipEntry(tempFile.getName());
                out.putNextEntry(e);
            }
            writeData(input,out);
            //close all
            tempOutput.close();
            input.close();
            tempFile.deleteOnExit();
            out.closeEntry();
        }
    }
}
