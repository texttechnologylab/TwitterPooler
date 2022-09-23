package QueryTest;

import com.mongodb.client.MongoClient;
import io.quarkus.mongodb.MongoClientName;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;


//@QuarkusTest
public class TimelinesLkpTest {
    @Inject
    @MongoClientName("tweets")
    MongoClient mongoClientTweets;

    @Test
    public void userTweetTimelineTest() throws URISyntaxException, IOException {
        /*String userId = "1392561470135808003";
        String endpointKey = "userTweetTimelineById";

        //get TwitterAccount
        TwitterAccount account = TwitterAccount.getAccountByName("Grzegorz");

        //initialize Query class
        Query query = new Query();
        query.setUserId(userId);

        //init Endpoint
        Endpoint endpointClass = new Endpoint();
        endpointClass.setEndp(Endpoint.getEndpoint(endpointKey, endpointClass.endpointList));

        //create uri
        URIBuilder uri = query.createTimelinesQuery(endpointClass.getEndp(),"10",null,query.getUserId(),null,null);

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

        //-------------getNextToken
        query.setNextToken((String)result.getJSONObject("meta").opt("next_token"));

        //create QueryAccount
        Instant instant = Instant.now();
        QueryAccount.createQueryAccount(uniqueID,userId,"",account.getName(),numberOfTweets,numberOfTweets, endpointKey, instant.toString(),null,null,"test");

        Assertions.assertTrue(oldNumberofDocuments != mongoCollection.countDocuments());

    }
    @Test
    public void userMentionTimelineTest() throws URISyntaxException, IOException {
        String userId = "1392561470135808003";
        String endpointKey = "userMentionTimelineById";

        //get TwitterAccount
        TwitterAccount account = TwitterAccount.getAccountByName("Grzegorz");

        //initialize Query class
        Query query = new Query();
        query.setUserId(userId);

        //init Endpoint
        Endpoint endpointClass = new Endpoint();
        endpointClass.setEndp(Endpoint.getEndpoint(endpointKey, endpointClass.endpointList));

        //create uri
        URIBuilder uri = query.createTimelinesQuery(endpointClass.getEndp(),"10",null,query.getUserId(),null,null);

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

        //-------------getNextToken
        query.setNextToken((String)result.getJSONObject("meta").opt("next_token"));

        //create QueryAccount
        Instant instant = Instant.now();
        QueryAccount.createQueryAccount(uniqueID,userId,"",account.getName(),numberOfTweets,numberOfTweets, endpointKey, instant.toString(),null,null,"test");

        Assertions.assertTrue(oldNumberofDocuments != mongoCollection.countDocuments());*/
    }
}
