package org.texttechnologylab.twitterpooler.tweets;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.texttechnologylab.twitterpooler.twitter.PoolAccount;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * PoolingStarterRunnable
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * Pooling job with several help functions.
 **/
public class PoolingStarterRunnable implements  Runnable{

    private String poolingArt;
    private MongoCollection<Document> mongoCollection;
    private MongoDatabase mongoClient;
    private String id;
    private String topics;
    private Map<String,Integer> poolValues;
    public PoolingStarterRunnable(MongoDatabase mongoClient, MongoCollection<Document> mongoCollection, String poolingArt, String id, String topics, Map<String,Integer> poolValues)
    {

        this.poolingArt = poolingArt;
        this.mongoCollection = mongoCollection;
        this.mongoClient = mongoClient;
        this.id = id;
        this.topics = topics;
        this.poolValues= poolValues;
    }
    /**
     * This function start the pooling process.
     */
    @Override
    public void run()
    {
        System.out.println(this.poolingArt + " pooling job startet");
        long startTime = System.currentTimeMillis();
        //init
        int numberOfPools = 0;
        int numberOfCollections = 0;
        String prefix="";
        Integer limit= poolValues.get("limit");
        Map<String, ArrayList<Document>> pools = new HashMap<String, ArrayList<Document>>();
        //choose pooling art
        if(this.poolingArt.equals("author"))
        {
            prefix = "A"+"L"+Integer.toString(limit);
            pools = startAuthorWisePooling(this.mongoCollection);
        }
        else if (this.poolingArt.equals("hashtag"))
        {
            prefix = "H"+"L"+Integer.toString(limit);
            pools = startHashtagPooling(this.mongoCollection);
        }
        else if(this.poolingArt.equals("temporalDays"))
        {
            prefix="TDays"+"S"+Integer.toString(poolValues.get("size"))+"L"+Integer.toString(limit);
            if (poolValues.get("size") == 1)
            {
                pools=startTemporalDayPooling(this.mongoCollection);
            }
            else
            {
                pools = startTemporalDaysPooling(this.mongoCollection, poolValues.get("size"));
            }
        }
        else if(this.poolingArt.equals("temporalHours"))
        {
            prefix="THours"+"S"+Integer.toString(poolValues.get("size"))+"L"+Integer.toString(limit);
            pools = startTemporalHoursPooling(this.mongoCollection, poolValues.get("size"));
        }
        else if (this.poolingArt.equals("burst"))
        {
            prefix="B"+"L"+Integer.toString(limit);
            pools = startBurstPooling(this.mongoCollection, topics, limit, poolValues.get("size"));
        }
        //create pool account, for better monitoring
        PoolAccount poolAccount = new PoolAccount();
        poolAccount.setQueryId(id);
        poolAccount.setPoolArt(prefix);
        poolAccount.setStatus("Running");
        poolAccount.setMulti("no available");
        poolAccount.setOne("no available");
        Instant instant = Instant.now();
        poolAccount.setCreatedAt(instant.toString());
        poolAccount.persist();
        //this hold time for mongoDB save
        long saveTime=0;
        //create collections
        try{


        for (var entry : pools.entrySet()) {
            //and not burst!
            //check doublets, because MongoDB collection can store only one Object with the same ObjectId
            ArrayList<Document> checked = new ArrayList<>();
            if(entry.getValue().size()>=limit)
            {
                checked = checkDoublets(entry.getValue());
            }
            else{checked= entry.getValue();}
            if(poolingArt.equals("burst") || checked.size()>=limit)
            {
                //save time
                long startTimeve = System.currentTimeMillis();

                String name = prefix + "-" + entry.getKey() + "-" + id;
                mongoClient.createCollection(name);
                MongoCollection collection = mongoClient.getCollection(name);
                //old to remove
                /*for (int i = 0; i < checked.size(); i++) {
                    try{
                        long startInsert = System.currentTimeMillis();
                    collection.insertOne(checked.get(i));
                        long enInsert = System.currentTimeMillis();
                        System.out.println("Insert took " + (enInsert - startInsert) + " milliseconds");
                    }

                    catch(Throwable e){System.out.println(e);}
                }*/
                long startInsert = System.currentTimeMillis();
                System.out.println("Insert start");
                collection.insertMany(checked);
                long enInsert = System.currentTimeMillis();
                saveTime = saveTime + (enInsert - startInsert);
                System.out.println("Insert took " + (enInsert - startInsert) + " milliseconds");


                numberOfPools = numberOfPools+1;
                numberOfCollections = numberOfCollections+ checked.size();
                long endTimeve = System.currentTimeMillis();
                System.out.println("Save took " + (endTimeve - startTimeve) + " milliseconds");
                System.out.println(entry.getKey() + " was created!");
            }
        }}
        catch (Exception e){System.out.println(e);
        }

        //update pool account
        poolAccount.setNumberOfPools(numberOfPools);
        poolAccount.setNumberOfCollections(numberOfCollections);
        poolAccount.setStatus("Complete");
        poolAccount.persistOrUpdate();
        System.out.println("Pool prefix"+id+ "account was created!");
        long endTime = System.currentTimeMillis();
        System.out.println("Pooling took " + (endTime - startTime) + " milliseconds");
        System.out.println("Save took " + saveTime + " milliseconds");
        System.out.println("Job is completed!");
    }
    /**
     * Looking for doublets in collection. Useful, because sometimes one tweet contains the same tag more than once.
     * @param toCheck list of tweets from relevant pool
     * @return ArrayList<Document> list without doublets
     */
    private ArrayList<Document> checkDoublets(ArrayList<Document> toCheck)
    {
        ArrayList<Document> result = new ArrayList<>();
        try{
        List<ObjectId> keys = new ArrayList<>();
        for(int i=0; i< toCheck.size();i++)
        {
            ObjectId id = (ObjectId) toCheck.get(i).get("_id");
            if(!keys.contains(id))
            {
                result.add(toCheck.get(i));
                keys.add(id);
            }

            System.out.println(id+"checked!");
        }}
        catch(Exception e){            System.out.println(e);
        }

        return result;
    }

    /**
     * Start burst pooling with manually set topics.
     * @param mongoCollection MongoDB collection of tweets
     * @param topics searched topics
     * @param limit brust score limit, topics below will be ignored
     * @param size pool size, the way tweets are organized (1 = 1Day, 2 = 2Days etc.)
     * @return
     */
    public Map startBurstPooling (MongoCollection<Document> mongoCollection, String topics, Integer limit, Integer size)
    {
        Map<String, ArrayList<Document>> M = new HashMap<>();
        //set of all messages pro day
        if(size == 1)
        {
            M = startTemporalDayPooling(mongoCollection);
        }
        else
        {
            M = startTemporalDaysPooling(mongoCollection, size);
        }
        //set of potential trending topic
        String[] R = topics.split(" ");

        //1. get brust-score from all terms inside R
        Map<String, ArrayList<Double>> termsBrustScoreList = new HashMap<>();
        for (int i = 0; i< R.length; i++)
        {
            ArrayList<Double> termBrustScore = calculateBrustScore(R[i],M);
            termsBrustScoreList.put(R[i], termBrustScore);
        }
        //2.Chose the terms
        ArrayList<String> termsResult = termsSelect(termsBrustScoreList, limit);

        //start terms pooling
        Map<String, ArrayList<Document>> pools = new HashMap<String, ArrayList<Document>>();

        FindIterable<Document> fi = mongoCollection.find();
        MongoCursor<Document> cursor = fi.iterator();
        try {
            while(cursor.hasNext()) {
                //String authorId = cursor.next().get("author_id").toString();
                Document docObject = Document.parse(cursor.next().toJson());
                String text = docObject.getString("text");
                //iterate through all terms and check if text contain the term
                for (int k = 0; k< termsResult.size();k++)
                {
                    String termName = termsResult.get(k);
                    if(text.toLowerCase().contains(termName.toLowerCase()))
                    {
                        //when text contain the term
                        if(pools.get(termName) != null) {
                            ArrayList<Document> temp = pools.get(termName);
                            temp.add(docObject);
                            pools.put(termName, temp);
                            System.out.println(termName + "added to list");
                        }
                        else
                        {
                            ArrayList<Document> temp = new ArrayList<>();
                            temp.add(docObject);
                            pools.put(termName, temp);
                            System.out.println(termName + "added to list");
                        }
                    }

                }
            }
        } finally {
            cursor.close();
        }
        return pools;
    }

    /**
     * Remove all terms under the limit
     * @param termsBrustScoreList terms with brust score
     * @param limit score limit
     * @return
     */
    public ArrayList<String> termsSelect(Map<String, ArrayList<Double>> termsBrustScoreList, Integer limit)
    {
        ArrayList<String> termsResult = new ArrayList<>();
        for (var term : termsBrustScoreList.entrySet()) {
            double score = 0;
            for(int i = 0; i<term.getValue().size(); i++)
            {
                if(term.getValue().get(i)>score)
                {
                    score = term.getValue().get(i);
                }
            }
            if (score >= limit)
            {
                termsResult.add(term.getKey());
            }
        }
        return  termsResult;
    }

    /**
     * Calculate terms brust score
     * @param R topic
     * @param M sorted tweets
     * @return
     */
    public ArrayList<Double> calculateBrustScore(String R, Map<String, ArrayList<Document>> M)
    {
        /*
         * D represent all days ---> M.length()
         * d represent 1 day ------> M[i]
         * M(R,d) tutaj mam twittery ktore zawieraja wszystkie termy z R napisane w dniu d
         * Mean(R) = 1/D * suma wszysthich M(R,d)
         *
         * */
        ArrayList<ArrayList<Document>> MRd = new ArrayList<>();
        int daysCount = 0;
        for (var entry : M.entrySet())
        {
            ArrayList<Document> tempDoc = entry.getValue();
            ArrayList<Document> MrdTemp = new ArrayList<>();
            //go through all Tweets from x day
            for(int i = 0; i<tempDoc.size(); i++)
            {
                String text = tempDoc.get(i).getString("text");
                if(text.toLowerCase().contains(R.toLowerCase()))
                {
                    MrdTemp.add(tempDoc.get(i));
                }
            }
            if(MrdTemp.size()>0) {
                MRd.add(daysCount, MrdTemp);
                daysCount += 1;
            }
        }
        //sum of all deDfrom MRd
        double sumAllMRd = 0;
        for (int j =0; j<MRd.size(); j++)
        {
            sumAllMRd = sumAllMRd + MRd.get(j).size();
        }
        double MeanR = sumAllMRd/ MRd.size();
        //calculate standarad deviation of MRd over the d day from D
        //frage: soll ich diese Mittelwert from M (all Messages) or MRd( Dataset only with messages which includes R)
        //calculate the deviations of each day
        double deviations = 0;
        for (int l =0; l<MRd.size(); l++)
        {
            double temp = Math.pow(MRd.get(l).size()-MeanR,2);
            deviations = deviations + temp;
        }
        //calculate varianz
        double varianz = deviations/MeanR;
        //calculate standard deviation
        double SDR = Math.sqrt(varianz);
        //calculate brust-score for each day
        ArrayList<Double> burstScoreList = new ArrayList<>();
        for(int m = 0;m<MRd.size();m++)
        {
            double burstScore = Math.abs(MRd.get(m).size()-MeanR)/SDR;
            burstScoreList.add(m, burstScore);
        }
        return burstScoreList;
    }

    /**
     * Start temporal hours pooling
     * @param mongoCollection .tweets collection
     * @param size pool size, the way tweets are organized (1 = 1Hour, 2 = 2Hours etc.)
     * @return Map
     */
    public Map startTemporalHoursPooling(MongoCollection<Document> mongoCollection, Integer size)
    {
        /*
        * 1 - every hour
        * 2 - 00-02, 02-04, 04-06, 06-08, 08-10, 10-12, 12-14, 14-16, 16-18, 18-20, 20-22, 22-24
        * 4 - 00-04, 04-08, 08-12, 12-16, 16-20, 20-24
        * 6 - 00-06, 06-12, 12-18, 18-24
        * 8 - 00-08, 08-16, 16-24
        * 12 - 00-12, 12-24
        * */
        Map<String, ArrayList<Document>> pools = new HashMap<String, ArrayList<Document>>();

        FindIterable<Document> fi = mongoCollection.find();
        MongoCursor<Document> cursor = fi.iterator();
        try {
            while(cursor.hasNext()) {
                //String authorId = cursor.next().get("author_id").toString();
                Document docObject = Document.parse(cursor.next().toJson());
                String strDate = docObject.getString("created_at");
                LocalDate inputDate = LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE_TIME);
                int inputHour = LocalDateTime.parse(strDate, DateTimeFormatter.ISO_DATE_TIME).getHour();
                int inputMinute = LocalDateTime.parse(strDate, DateTimeFormatter.ISO_DATE_TIME).getMinute();
                //function for creating key
                String temporalKey = createTempHoursKey(size, inputHour,inputDate.toString());
                //String temporalKey = inputDate.toString()+"H"+inputHour;
                if(pools.containsKey(temporalKey))
                {
                    ArrayList<Document> temp = pools.get(temporalKey);
                    temp.add(docObject);
                    pools.put(temporalKey, temp);
                    System.out.println(temporalKey+"added to list");
                }
                else
                {
                    ArrayList<Document> temp  = new ArrayList<Document>();
                    temp.add(docObject);
                    pools.put(temporalKey, temp);
                    System.out.println(temporalKey+"added to list");
                }
            }
        } finally {
            cursor.close();
        }
        return pools;
    }

    /**
     * Create key for temporal hours pool (For details look into documentation)
     * @param size pool size, the way tweets are organized (1 = 1Hour, 2 = 2Hours etc.)
     * @param inputHour hour of tweet
     * @param date date of tweet
     * @return key (Example : 2022-01-01T12-14)
     */
    private String createTempHoursKey(Integer size, Integer inputHour, String date)
    {
        String key=""; //date+H02:04
        List<Integer> keys = new ArrayList<>();
        for (int i = 0; i<24; i=i)
        {
                if(i==0)
                {
                    keys.add(i);
                }
                i = i + size;
                keys.add(i);
        }
        for (int j = 0 ; j< keys.size();j++)
        {
            if(inputHour<keys.get(j))
            {
                //String ble = String.format("%02d", Integer.toString(keys.get(j-1)));
                key =date + "T"+String.format("%02d", keys.get(j-1))+"-"+String.format("%02d", keys.get(j));
                break;
            }
            if(inputHour==24)
            {
                key =date + "T"+String.format("%02d", 0)+"-"+String.format("%02d", keys.get(1));
                break;
            }
        }
        return key;
    }

    /**
     * Start temporal 1 day pooling
     * @param mongoCollection tweets collection
     * @return Map
     */
    public Map startTemporalDayPooling(MongoCollection<Document> mongoCollection)
    {
        Map<String, ArrayList<Document>> pools = new HashMap<String, ArrayList<Document>>();

        FindIterable<Document> fi = mongoCollection.find();
        MongoCursor<Document> cursor = fi.iterator();
        try {
            while(cursor.hasNext()) {
                Document docObject = Document.parse(cursor.next().toJson());
                String strDate = docObject.getString("created_at");
                LocalDate inputDate = LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE_TIME);
                if(pools.containsKey(inputDate.toString()))
                {
                    ArrayList<Document> temp = pools.get(inputDate.toString());
                    temp.add(docObject);
                    pools.put(inputDate.toString(), temp);
                    System.out.println(inputDate.toString()+"added to list");
                }
                else
                {
                    ArrayList<Document> temp = new ArrayList<Document>();
                    temp.add(docObject);
                    pools.put(inputDate.toString(), temp);
                    System.out.println(inputDate.toString()+"added to list");
                }
            }
        } finally {
            cursor.close();
        }
        return pools;
    }

    /**
     * Start temporal x days pooling
     * @param mongoCollection tweets collection
     * @param size pool size, the way tweets are organized (1 = 1Day, 2 = 2Days etc.)
     * @return Map
     */
    public Map startTemporalDaysPooling(MongoCollection<Document> mongoCollection, Integer size)
    {
        Map<String, ArrayList<Document>> pools = new HashMap<String, ArrayList<Document>>();

        FindIterable<Document> fi = mongoCollection.find();
        MongoCursor<Document> cursor = fi.iterator();
        try {
            while(cursor.hasNext()) {
                Document docObject = Document.parse(cursor.next().toJson());
                String strDate = docObject.getString("created_at");
                LocalDate inputDate = LocalDate.parse(strDate, DateTimeFormatter.ISO_DATE_TIME);
                String temporalKey = createTempDaysKey(size, inputDate);
                if(pools.containsKey(temporalKey))
                {
                    ArrayList<Document> temp = pools.get(temporalKey);
                    temp.add(docObject);
                    pools.put(temporalKey, temp);
                    System.out.println(temporalKey+"added to list");
                }
                else
                {
                    ArrayList<Document> temp = new ArrayList<Document>();
                    temp.add(docObject);
                    pools.put(temporalKey, temp);
                    System.out.println(temporalKey+"added to list");
                }
            }
        } finally {
            cursor.close();
        }
        return pools;
    }

    /**
     * Create key for temporal days pool
     * @param size pool size, the way tweets are organized (1 = 1Day, 2 = 2Days etc.)
     * @param date date of tweet
     * @return key
     */
    private String createTempDaysKey(Integer size, LocalDate date)
    {
        int dayOfYear = date.getDayOfYear();
        String key="";
        List<Integer> keys = new ArrayList<>();
        for (int i = 1; i<date.lengthOfYear(); i=i)
        {
            if(i==1)
            {
                keys.add(i);
            }
            i = i + size;
            keys.add(i);
        }
        keys.set(keys.size()-1, date.lengthOfYear());

        for (int j = 0 ; j< keys.size();j++)
        {
            if(dayOfYear<keys.get(j))
            {
                key =date.getYear() + "-"+keys.get(j-1)+"/"+keys.get(j);
                break;
            }
        }

        return key;
    }

    /**
     * Start hashtag pooling
     * @param mongoCollection tweet collection
     * @return Map
     */
    public Map startHashtagPooling(MongoCollection<Document> mongoCollection)
    {
        Map<String, ArrayList<Document>> pools = new HashMap<String, ArrayList<Document>>();

        FindIterable<Document> fi = mongoCollection.find();
        MongoCursor<Document> cursor = fi.iterator();
        try {
            while(cursor.hasNext()) {
                //get all hashtags
                Document docObject = Document.parse(cursor.next().toJson());
                if(((Document)docObject.get("entities")).get("hashtags") != null)
                {
                    ArrayList<Document> tempHashtags = (ArrayList<Document>)((Document)docObject.get("entities")).get("hashtags");
                    for (int i =0; i<tempHashtags.size(); i++)
                    {
                        String tag = tempHashtags.get(i).getString("tag").toLowerCase();
                        if(pools.containsKey(tag))
                        {
                            ArrayList<Document> temp = pools.get(tag);
                            temp.add(docObject);
                            pools.put(tag, temp);
                            System.out.println(tag+"added to list");
                        }
                        else
                        {
                            ArrayList<Document> temp = new ArrayList<Document>();
                            temp.add(docObject);
                            pools.put(tag, temp);
                            System.out.println(tag+"added to list");
                        }
                    }
                }
            }
        } finally {
            cursor.close();
        }
        return pools;
    }

    /**
     * Start author wise pooling
     * @param mongoCollection tweet collection
     * @return Map
     */
    public static Map startAuthorWisePooling(MongoCollection<Document> mongoCollection)
    {
        Map<String, ArrayList<Document>> pools = new HashMap<String, ArrayList<Document>>();

        FindIterable<Document> fi = mongoCollection.find();
        MongoCursor<Document> cursor = fi.iterator();
        try {
            while(cursor.hasNext()) {
                //String authorId = cursor.next().get("author_id").toString();
                Document docObject = Document.parse(cursor.next().toJson());
                String authorId = docObject.getString("author_id");
                if(pools.containsKey(authorId))
                {
                    ArrayList<Document> temp = pools.get(authorId);
                    temp.add(docObject);
                    pools.put(authorId, temp);
                    System.out.println(authorId+"added to list");
                }
                else
                {
                    ArrayList<Document> temp = new ArrayList<Document>();
                    temp.add(docObject);
                    pools.put(authorId, temp);
                    System.out.println(authorId+"added to list");
                }
            }
        } finally {
            cursor.close();
        }
        return pools;
    }
}
