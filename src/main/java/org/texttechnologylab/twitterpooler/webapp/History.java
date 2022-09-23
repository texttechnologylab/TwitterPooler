package org.texttechnologylab.twitterpooler.webapp;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.quarkiverse.freemarker.TemplatePath;
import io.quarkus.mongodb.MongoClientName;
import io.quarkus.mongodb.panache.PanacheQuery;
import org.texttechnologylab.twitterpooler.twitter.PoolAccount;
import org.texttechnologylab.twitterpooler.twitter.QueryAccount;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.MediaType.TEXT_HTML;

/**
 * History
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * This class provides history endpoint.
 **/
@Path("history")
public class History {
    @Inject
    @TemplatePath("history.ftl")
    Template home;

    @Inject
    @MongoClientName("tweets")
    MongoClient mongoClientTweets;
    /**
     * Main function for history endpoint
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    @GET
    @Produces(TEXT_HTML)
    public String history() throws IOException, TemplateException {

        //PanacheQuery<QueryAccount> queryAcc = QueryAccount.findAll();
        PanacheQuery<QueryAccount> queryAcc = QueryAccount.find("status ='Complete'");
        List<QueryAccount> queryAccounts = queryAcc.list();

        PanacheQuery<PoolAccount> poolAcc = PoolAccount.find("status ='Complete'");
        List<PoolAccount> poolAccounts = poolAcc.list();

        Map<String, Object> params = new HashMap<>();
        params.put("queryAccounts",queryAccounts);
        params.put("poolAccounts",poolAccounts);

        StringWriter stringWriter = new StringWriter();
        home.process(params,stringWriter);
        return stringWriter.toString();
    }

    @GET
    @Path("delete")
    public void delete(@Context UriInfo info)
    {
        String type = info.getQueryParameters().get("type").get(0);
        String queryId = info.getQueryParameters().get("queryId").get(0);
        if (type.equals("query"))
        {
            QueryAccount account = QueryAccount.getQueryAccount(queryId);
            account.delete();

            MongoDatabase db = mongoClientTweets.getDatabase("Tweets");
            MongoCollection coll = db.getCollection(queryId);
            coll.drop();
        }
        else
        {

            String art = info.getQueryParameters().get("art").get(0);
            MongoDatabase db = mongoClientTweets.getDatabase("Pools");

            ArrayList<String> listOfPools = new ArrayList<String>();
            MongoIterable<String> list = db.listCollectionNames();
            for (String name : list)
            {
                if(name.contains(art) && name.contains(queryId))
                {
                    listOfPools.add(name);
                }
            }

            for(int i =0;i< listOfPools.size();i++)
            {
                MongoCollection coll = db.getCollection(listOfPools.get(i));
                coll.drop();
            }


            PoolAccount account = PoolAccount.getPoolAccount(queryId,art);
            account.delete();
        }
    }
}
