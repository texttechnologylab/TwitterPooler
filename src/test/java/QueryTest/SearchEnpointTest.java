package QueryTest;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import database.DBHelper;
import io.quarkus.mongodb.MongoClientName;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.texttechnologylab.uimadb.wrapper.mongo.serilization.exceptions.CasSerializationException;
import org.texttechnologylab.uimadb.wrapper.mongo.serilization.exceptions.SerializerInitializationException;
import org.texttechnologylab.uimadb.wrapper.mongo.serilization.exceptions.UnknownFactoryException;
import org.xml.sax.SAXException;
import tweets.*;
import twitter.QueryAccount;
import twitter.TwitterAccount;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.UUID;


@QuarkusTest
public class SearchEnpointTest {
    @Inject
    @MongoClientName("tweets")
    MongoClient mongoClientTweets;

    @Test
    public void getHashtagTweetsWithMaxResult() throws URISyntaxException, IOException, SerializerInitializationException, UnknownFactoryException, CasSerializationException, SAXException {
        /*
        * Plan
        * 1.Check Count
        * 2.Check if the account has enough free tweets
        * 3.Check if the query does not have next_token
        * 4.Create uniqueID
        * 5.Create uri
        * 6.Send request
        * 7.Create mongo query collection
        * 8.Save in db
        * 9.Create QueryAccount
        * 10.Update Twitter Account
        * 11.Assertions
         * */
        /*String hashtag = "#granica #bialorus";
        //String hashtag = "#obama";
        String maxResult = "30";
        String endpointCount="countRecent";
        String endpointSearch="searchRecent";
        String startTime = "2021-11-13T19:30:00Z";
        String endTime = "2021-11-13T19:40:00Z";

        //-------------check count
        //get TwitterAccount
        TwitterAccount account = TwitterAccount.getAccountByName("Grzegorz");

        //initialize Query class

        Query query = new Query();
        query.setHashtag(hashtag);
        query.setMaxResults(maxResult);

        //get endpoint
        Endpoint endpoint = new Endpoint();
        endpoint.setEndp(Endpoint.getEndpoint(endpointCount, endpoint.endpointList));
        //create uri
        URIBuilder uri = query.createCountQuery(query.getHashtag(),null,null,endpoint.getEndp());

        RequestHandler requestHandler = new RequestHandler();
        HttpEntity entity = requestHandler.sendRequest(uri, account.getBearerToken(), requestHandler.client);
        JSONObject result = requestHandler.parseToJson(entity);

        query.setTweetCount((int)((JSONObject)result.get("meta")).get("total_tweet_count"));

        //---------------Check if the account has enough free tweets


        boolean tweetCapLimit = false;
        if (account.getTwitterLimit()-account.getTwitterCap()< query.getTweetCount())
        {
            tweetCapLimit = true;
        }

        //---------------Check if the query does not have next_token
        if(query.getNextToken() != null)
        {
            //do something else
        }
        else
        {
            //---------------------Create uniqueID
            String uniqueID = UUID.randomUUID().toString();
            query.setUniqueId(uniqueID);

            //get search endpoint
            endpoint.setEndp(Endpoint.getEndpoint(endpointSearch, endpoint.endpointList));
            //---------------------Create uri
            URIBuilder uriSearch = query.createSearchQuery(query.getMaxResults(),null,query.getHashtag(),startTime,null,endpoint.getEndp());

            //---------------------Send request
            HttpEntity entitySearch = requestHandler.sendRequest(uriSearch, account.getBearerToken(), requestHandler.client);
            JSONObject resultSearch = requestHandler.parseToJson(entitySearch);

            //---------------------Create mongo query collection
            var mongoClient = mongoClientTweets.getDatabase("Tweets");
            mongoClient.createCollection(uniqueID);
            MongoCollection<Document> mongoCollection = mongoClient.getCollection(uniqueID);
            long oldNumberofDocuments = mongoCollection.countDocuments();


            //Save result in database
            int numberOfTweets = ((JSONArray)resultSearch.get("data")).length();

            //create includes
            JSONObject tweetIncludes = (JSONObject)resultSearch.get("includes");
            query.setIncludesFields(tweetIncludes);

            for (int i=0; i<numberOfTweets;i++)
            {
                //create tweet document and save collection
                //mongoCollection.insertOne(DBHelper.createTweetDocument(resultSearch,i, uniqueID));
                mongoCollection.insertOne(DBHelper.createTweetDocument(resultSearch,i, uniqueID,
                        query.getMedia(), query.getTweets(), query.getUsers(), query.getPoll(), query.getPlaces()));

            }

            //-------------getNextToken
            query.setNextToken((String)resultSearch.getJSONObject("meta").opt("next_token"));

            //create QueryAccount
            Instant instant = Instant.now();
            QueryAccount.createQueryAccount(uniqueID,query.getHashtag(),query.getNextToken(),account.getName(),query.getTweetCount(),numberOfTweets,endpointSearch,instant.toString(),
                    null,null,"test");

            //-------------update TwitterAccount
            TwitterAccount.updateTweetCount(account.getName(), numberOfTweets);

            //Assertions
            MongoCollection<Document> mongoCollectionTest = mongoClient.getCollection(uniqueID);
            Assertions.assertTrue(mongoCollectionTest.countDocuments() == numberOfTweets);
            Assertions.assertTrue(QueryAccount.findNextToken(uniqueID).equals(query.getNextToken()));
        }*/
    }

    @Test
    public void getHashtagTweetsWithNextToken() throws Exception {
        /*
         * Plan
         * 1.Get QueryAccount
         * 2.Get TwitterAccount
         * 3.Check CapLimit
         * 4.Get query collection
         * 5.Initialize Query class
         * 6.Create uri
         * 7.Send request
         * 8.Save in db
         * 9.Update QueryAccount
         * 10.Update TwitterAccount
         * 11.Asserstion
         * */
        /*String queryId = "24274aef-73ec-451d-b3d8-e61f1aac0eb5";
        //------------get QueryAccount
        QueryAccount queryAccount = QueryAccount.getQueryAccount(queryId);

        //------------get TwitterAccount
        TwitterAccount twitterAccount = TwitterAccount.getAccountByName(queryAccount.getUser());

        //------------to see how many tweets are still available for download
        int tweetsToDownload = queryAccount.getAllTweets() - queryAccount.getTweetCount();

        //-------------check available cap limit
        int availableCaps = twitterAccount.getTwitterLimit()- twitterAccount.getTwitterCap();

        if (availableCaps >= 100)
        {
            //we can download all 100 tweets
            //----------get QueryCollection
            MongoDatabase mongoClient = mongoClientTweets.getDatabase("Tweets");
            MongoCollection<Document> mongoCollection = mongoClient.getCollection(queryId);
            long oldNumberofDocuments = mongoCollection.countDocuments();

            //get endpoint
            Endpoint endpoint = new Endpoint();
            endpoint.setEndp(Endpoint.getEndpoint(queryAccount.getEndpoint(), endpoint.endpointList));

            //----------initialize Query class
            Query query = new Query();
            query.setQuery(queryAccount.getQuery());
            query.setNextToken(queryAccount.getNextToken());
            query.setEndpointValue(endpoint.getEndp());
            query.setMaxResults("20");

            //----------create uri
            URIBuilder uriSearch = query.createSearchQuery(query.getMaxResults(),query.getNextToken(),query.getQuery(),null,null,query.getEndpointValue());

            //---------------------Send request
            RequestHandler requestHandler = new RequestHandler();
            HttpEntity entitySearch = requestHandler.sendRequest(uriSearch, twitterAccount.getBearerToken(), requestHandler.client);
            JSONObject resultSearch = requestHandler.parseToJson(entitySearch);

            //Save result in database
            int numberOfTweets = ((JSONArray)resultSearch.get("data")).length();

            //create includes
            JSONObject tweetIncludes = (JSONObject)resultSearch.get("includes");
            query.setIncludesFields(tweetIncludes);

            for (int i=0; i<numberOfTweets;i++)
            {
                //create tweet document and save collection
                mongoCollection.insertOne(DBHelper.createTweetDocument(resultSearch,i, queryAccount.getQueryId(),
                        query.getMedia(), query.getTweets(), query.getUsers(), query.getPoll(), query.getPlaces()));
            }

            //-------------getNextToken and update QueryAccount
            if((String)resultSearch.getJSONObject("meta").opt("next_token") != null)
            {
                query.setNextToken((String)resultSearch.getJSONObject("meta").get("next_token"));
                QueryAccount.updateNextToken(queryAccount.getQueryId(), query.getNextToken());
            }
            QueryAccount.updateTweetCount(queryAccount.getQueryId(), numberOfTweets);

            //update TwitterAccount
            TwitterAccount.updateTweetCount(twitterAccount.getName(),numberOfTweets);

            //Assertions
            Assertions.assertTrue(mongoCollection.countDocuments() == numberOfTweets+oldNumberofDocuments);
            Assertions.assertTrue(QueryAccount.findNextToken(queryAccount.getQueryId()).equals(query.getNextToken()));

        }

        else
        {
            //------ we have to match max_result to availableCaps
        }*/

    }
}

