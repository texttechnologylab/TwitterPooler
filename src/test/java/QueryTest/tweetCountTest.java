package QueryTest;

import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tweets.Endpoint;
import tweets.Query;
import tweets.RequestHandler;
import twitter.TwitterAccount;

import java.io.IOException;
import java.net.URISyntaxException;

@QuarkusTest
public class tweetCountTest {

    @Test
    public void countTestWithoutDate() throws URISyntaxException, IOException {
        String searchQuery = "#przemysl";
        String startTime = "2021-11-10T00:00:00Z";
        String endpointValue = "countRecent";
        //get TwitterAccount
        TwitterAccount account = TwitterAccount.getAccountByName("Grzegorz");

        //initialize Query class
        Query query = new Query();
        query.setHashtag(searchQuery);

        //get endpoint
        Endpoint endpoint = new Endpoint();
        endpoint.setEndp(Endpoint.getEndpoint(endpointValue, endpoint.endpointList));

        //create uri
        URIBuilder uri = query.createCountQuery(query.getHashtag(),null,null,query.getEndpointValue());

        RequestHandler requestHandler = new RequestHandler();
        HttpEntity entity = requestHandler.sendRequest(uri, account.getBearerToken(), requestHandler.client);
        JSONObject result = requestHandler.parseToJson(entity);

        query.setTweetCount((int)((JSONObject)result.get("meta")).get("total_tweet_count"));
        String stop = "";
        Assertions.assertTrue(73 == query.getTweetCount());
    }

    @Test
    public void countTestWithDate() throws URISyntaxException, IOException {
        String searchQuery = "#przemysl";
        String startTime = "2021-11-10T00:00:00Z";
        String endpointValue = "countRecent";

        //get TwitterAccount
        TwitterAccount account = TwitterAccount.getAccountByName("Grzegorz");

        //initialize Query class
        Query query = new Query();
        query.setHashtag(searchQuery);
        query.setStartTime(startTime);

        //get endpoint
        Endpoint endpoint = new Endpoint();
        endpoint.setEndp(Endpoint.getEndpoint(endpointValue, endpoint.endpointList));

        //create uri
        URIBuilder uri = query.createCountQuery(query.getHashtag(),query.getStartTime(),null,query.getEndpointValue());

        RequestHandler requestHandler = new RequestHandler();
        HttpEntity entity = requestHandler.sendRequest(uri, account.getBearerToken(), requestHandler.client);
        JSONObject result = requestHandler.parseToJson(entity);

        query.setTweetCount((int)((JSONObject)result.get("meta")).get("total_tweet_count"));
        String stop = "";
        Assertions.assertTrue(27 == query.getTweetCount());
    }
}
