package QueryTest;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import database.DBHelper;
import io.quarkus.mongodb.MongoClientName;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tweets.Endpoint;
import tweets.Query;
import tweets.RequestHandler;
import twitter.QueryAccount;
import twitter.TwitterAccount;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.*;

import org.json.*;

import javax.inject.Inject;


@QuarkusTest
public class TweetLkpTest {
    @Inject
    @MongoClientName("tweets")
    MongoClient mongoClientTweets;

    @Test
    public void getTweet() throws URISyntaxException, IOException{
        /*String endpoint = "tweetLookup";
        String tweetId = "1454678518969864200,1299530165463199747";
        //String tweetId = "1299530165463199747";

        //get TwitterAccount
        TwitterAccount account = TwitterAccount.getAccountByName("Grzegorz");

        //initialize Query class
        Query query = new Query();
        query.setTweetId(tweetId);

        //init Endpoint
        Endpoint endpointClass = new Endpoint();
        endpointClass.setEndp(Endpoint.getEndpoint(endpoint, endpointClass.endpointList));

        //create uri
        URIBuilder uri = query.createTweetQuery(endpointClass.getEndp(), query.getTweetId());

        RequestHandler requestHandler = new RequestHandler();
        HttpEntity entity = requestHandler.sendRequest(uri, account.getBearerToken(), requestHandler.client);
        JSONObject result = requestHandler.parseToJson(entity);

        // create uniqueID
        String uniqueID = UUID.randomUUID().toString();

        // create Query collection
        var mongoClient = mongoClientTweets.getDatabase("Tweets");
        mongoClient.createCollection(uniqueID);
        MongoCollection<Document> mongoCollection = mongoClient.getCollection(uniqueID);
        long oldNumberofDocuments = mongoCollection.countDocuments();


        JSONObject tweetIncludes = (JSONObject)result.get("includes");
        query.setIncludesFields(tweetIncludes);
        //Save result in database
        int numberOfTweets = ((JSONArray)result.get("data")).length();
        for (int i=0; i<numberOfTweets;i++)
        {
            mongoCollection.insertOne(DBHelper.createTweetDocument(result,i, uniqueID,
                    query.getMedia(), query.getTweets(), query.getUsers(), query.getPoll(), query.getPlaces()));
        }


        //create QueryAccount
        Instant instant = Instant.now();
        QueryAccount.createQueryAccount(uniqueID,tweetId,"",account.getName(),numberOfTweets,numberOfTweets, endpoint, instant.toString(),null,null,"test");

        Assertions.assertTrue(oldNumberofDocuments != mongoCollection.countDocuments());
*/
    }
}
