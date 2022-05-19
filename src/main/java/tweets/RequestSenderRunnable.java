package tweets;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import database.DBHelper;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;
import org.bson.Document;
import org.hucompute.textimager.client.TextImagerClient;
import org.json.JSONArray;
import org.json.JSONObject;
import twitter.QueryAccount;
import twitter.TwitterAccount;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * RequestSenderRunnable
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * Request job with several help functions.
 **/
public class RequestSenderRunnable implements Runnable{

    private String accountName;
    private Query query;
    private MongoDatabase mongoClient;
    private TextImagerClient client;
    private String STools;
    public RequestSenderRunnable(String accountName, Query query, MongoDatabase mongoClient, TextImagerClient client, String STools)
    {

        this.accountName = accountName;
        this.query = query;
        this.mongoClient = mongoClient;
        this.client = client;
        this.STools = STools;
    }
    /**
     * This function start the request sender job.
     */
    @Override
    public void run() {
        System.out.println("Job startet"+ " : queryid= "+query.getUniqueId());
        //long instants = Instant.now().toEpochMilli();
        //System.out.println(instants);
        System.out.println("Job Startet");
        boolean hasNextToken = true;
        int counterMaxResult = 0;
        int maxResult = 0;
        boolean tweetCapLimit = false;
        String queryStatus = "";
        Query query = this.query;
        int testcount=0;
        //fallback for Liked endpoint, because nextToken is always available
        LocalDateTime lastTweetDate = null;

        //-------------check count
        //get TwitterAccount
        TwitterAccount account = TwitterAccount.getAccountByName(this.accountName);


        //set maxResult if exist
        if(query.getMaxResults() != null && !query.getMaxResults().equals(""))
        {
            maxResult = Integer.parseInt(query.getMaxResults());
        }
        else
        {
            maxResult = query.getTweetCount();
            query.setMaxResults(Integer.toString(maxResult));
        }
        while (hasNextToken)
        {


            //get search endpoint
            Endpoint endpoint = new Endpoint();
            endpoint.setEndp(Endpoint.getEndpoint(query.getEndpointValue(), endpoint.endpointList));

            //---------------------Create uri
            URIBuilder uriSearch = null;
            try {
                uriSearch = createQuery(query, endpoint.getEndp());
            } catch (URISyntaxException | UnsupportedEncodingException e) {
                e.printStackTrace();
                System.out.println(e);
            }

            //---------------------Send request
            RequestHandler requestHandler = new RequestHandler();
            HttpEntity entitySearch = null;
            try {
                entitySearch = requestHandler.sendRequest(uriSearch, account.getBearerToken(), requestHandler.client);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                System.out.println(e);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e);
            }


            JSONObject resultSearch = null;
            try {
                resultSearch = requestHandler.parseToJson(entitySearch);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e);
            }

            //check if there is new tweet (because liked endpoint has only 1 nextToken end it is always enable)
            if(query.getEndpointValue().equals("liked"))
            {
                if(lastTweetDate == null)
                {
                    String tempDAte = resultSearch.getJSONArray("data").getJSONObject(0).get("created_at").toString();
                    lastTweetDate = LocalDateTime.parse(tempDAte, DateTimeFormatter.ISO_ZONED_DATE_TIME);
                }
                else
                {
                    int len = resultSearch.getJSONArray("data").length();
                    LocalDateTime newTweetDate = LocalDateTime.parse(resultSearch.getJSONArray("data").getJSONObject(len-1).get("created_at").toString(),DateTimeFormatter.ISO_ZONED_DATE_TIME);
                    if(newTweetDate.compareTo(lastTweetDate)<0)
                        {
                            lastTweetDate = LocalDateTime.parse(resultSearch.getJSONArray("data").getJSONObject(0).get("created_at").toString(),DateTimeFormatter.ISO_ZONED_DATE_TIME);
                        }
                    else
                    {
                        queryStatus = "Complete";
                        QueryAccount.updateStatus(query.uniqueId, "Complete");
                        System.out.println("Mam Cie!");
                        break;
                    }
                }
            }
            //check if collection exist
            boolean collectionExists = collectionExists(query.uniqueId, mongoClient);
            //if not exist create
            if(!collectionExists)
            {
                mongoClient.createCollection(query.uniqueId);
                System.out.println("Created tweet collection : "+query.uniqueId);
            }
            MongoCollection<Document> mongoCollection = mongoClient.getCollection(query.uniqueId);

            //Save result in database
            int numberOfTweets = ((JSONArray) resultSearch.get("data")).length();

            //create includes
            JSONObject tweetIncludes = (JSONObject) resultSearch.get("includes");
            query.setIncludesFields(tweetIncludes);

            //new added
            //ArrayList<Document> checked = new ArrayList<>();
            for (int i = 0; i < numberOfTweets; i++) {
                //create tweet document and save collection
                try{
                if (i == 0 && !collectionExists)
                {
                    createQueryAccount(query,"Running", account,numberOfTweets);
                }
                    Document doc = DBHelper.createTweetDocument(resultSearch, i, query.uniqueId,
                            query.getMedia(), query.getTweets(), query.getUsers(), query.getPoll(), query.getPlaces(), client, STools);
                mongoCollection.insertOne(doc);

                testcount = testcount+1;
                System.out.println(testcount+ "tweet was added");
                    //new added
                    /*checked.add(doc);
                    if (i==numberOfTweets-1)
                    {
                        mongoCollection.insertMany(checked);
                        checked.clear();
                    }*/
                }

                catch (Throwable e)
                {
                    System.out.println(e);
                }

            }
            //check date range
            checkDateRange(query.uniqueId,((JSONArray) resultSearch.get("data")).getJSONObject(numberOfTweets-1).get("created_at").toString() ,((JSONArray) resultSearch.get("data")).getJSONObject(0).get("created_at").toString());
            //-------------getNextToken
            query.setNextToken((String) resultSearch.getJSONObject("meta").opt("next_token"));

            //create QueryAccount if not exist
            if(!collectionExists)
            {
                /*Instant instant = Instant.now();
                QueryAccount.createQueryAccount(query.uniqueId, query.getQuery(), query.getNextToken(), account.getName(), query.getTweetCount(), numberOfTweets, query.getEndpointValue(), instant.toString(),
                        query.getStartTime(), query.getEndTime(), queryStatus);
                System.out.println("Created QueryAccount : "+query.uniqueId);*/
            }
            else //else update QueryAccount
            {
                QueryAccount.updateNextToken(query.uniqueId, query.getNextToken());
                QueryAccount.updateTweetCount(query.uniqueId, numberOfTweets);
                QueryAccount.updateStatus(query.uniqueId, queryStatus);
            }
            //-------------update TwitterAccount
            //only for search endpoint
            if(query.getEndpointValue().contains("search") || query.getEndpointValue().contains("Timeline") || query.getEndpointValue().contains("liked"))
            {
                TwitterAccount.updateTweetCount(account.getName(), numberOfTweets);
            }
            counterMaxResult = counterMaxResult+numberOfTweets;
            if (query.getMaxResults()!=null)
            {
                int newMaxResult = Integer.parseInt(query.getMaxResults())-numberOfTweets;
                query.setMaxResults(Integer.toString(newMaxResult));}

            //check tweet capLimit
            if (account.getTwitterLimit()-account.getTwitterCap()< query.getTweetCount())
            {
                System.out.println("Account tweet cap limit was reached");
                //update status queryAccount stopped
                QueryAccount.updateStatus(query.uniqueId, "Stopped");
                break;
            }


            else if(query.getNextToken() == null || (numberOfTweets<100)&&query.getEndpointValue().equals("liked"))// || numberOfTweets<100
            {
                hasNextToken = false;
                //update status
                queryStatus = "Complete";
                QueryAccount.updateStatus(query.uniqueId, "Complete");
            }

            else if(maxResult > 0 && counterMaxResult >= maxResult)
            {
                System.out.println("Max result was reached");
                //update status stopped
                MongoCollection<Document> collection = mongoClient.getCollection(query.uniqueId);
                //QueryAccount.updateStatus(query.uniqueId, "Stopped");
                QueryAccount.updateStatus(query.uniqueId, "Complete");
                QueryAccount.updateNextToken(query.uniqueId, query.getNextToken());
                break;
            }
            else
            {
                queryStatus="Running";
                QueryAccount.updateStatus(query.uniqueId, "Running");
            }

        }

        //long instant = Instant.now().toEpochMilli();
        //System.out.println(instant);
        System.out.println("Job finish");

    }

    /**
     * This function select and create relevant URI
     * @param query query
     * @param endpoint endpoint
     * @return URI
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     */
    private URIBuilder createQuery(Query query, String endpoint) throws URISyntaxException, UnsupportedEncodingException {
        URIBuilder uriBuilder = new URIBuilder();
        String endpointKey = query.getEndpointValue();

        if(endpointKey.contains("search"))
        {
            uriBuilder = query.createSearchQuery(query.getMaxResults(), query.getNextToken(), query.getQuery(), query.getStartTime(), query.getEndTime(), endpoint);
        }
        else if (endpointKey.equals("userTweetTimelineById") || endpointKey.equals("userMentionTimelineById"))
        {
            uriBuilder = query.createTimelinesQuery(endpoint, query.getMaxResults(),query.getNextToken(), query.getQuery(), query.getStartTime(), query.getEndTime());
        }
        else if (endpointKey.equals("liked"))
        {
            uriBuilder = query.createLikedQuery(endpoint, query.getQuery(), query.getMaxResults(),query.getNextToken());
        }
        else if (endpointKey.equals("liking"))
        {
            uriBuilder = query.createLikingQuery(endpoint, query.getQuery());
        }
        return uriBuilder;
    }

    //https://stackoverflow.com/questions/31909247/mongodb-3-java-check-if-collection-exists
    /**
     * Check if MongoDB collection exist
     * @param collectionName collection name (queryId)
     * @param mongoClient database
     * @return boolean
     */
    public boolean collectionExists(final String collectionName, MongoDatabase mongoClient) {
        MongoIterable<String> collectionNames = mongoClient.listCollectionNames();
        for (final String name : collectionNames) {
            if (name.equalsIgnoreCase(collectionName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create QueryAccount in Query collection
     * @param query query
     * @param status job status
     * @param account twitter dev account
     * @param numberOfTweets number of downloaded tweets
     */
    private void createQueryAccount(Query query, String status,TwitterAccount account, int numberOfTweets)
    {
        Instant instant = Instant.now();
        QueryAccount.createQueryAccount(query.uniqueId, query.getQuery(), query.getNextToken(), account.getName(), query.getTweetCount(), numberOfTweets, query.getEndpointValue(), instant.toString(),
                query.getStartTime(), query.getEndTime(), status, query.getSTools());
        System.out.println("Created QueryAccount : "+query.uniqueId);
    }

    /**
     * Check if query already exists
     * @param queryId
     * @param firstDate
     * @param lastDate
     */
    private void checkDateRange(String queryId, String firstDate, String lastDate)
    {
        LocalDate firstNewDate=LocalDate.parse(firstDate, DateTimeFormatter.ISO_DATE_TIME);
        LocalDate lastNewDate=LocalDate.parse(lastDate, DateTimeFormatter.ISO_DATE_TIME);

        //get QueryAccount
        QueryAccount account = QueryAccount.getQueryAccount(queryId);
        //if daterange empty then create i
        if(account.getDateRange().equals(""))
        {
            String newRange = firstNewDate.toString()+"/"+lastNewDate.toString();
            account.setDateRange(newRange);
            account.persistOrUpdate();
        }
        else{
        String test = "2012-10-10";
        LocalDate firstOldDate=null;
        LocalDate lastOldDate=null;

        var dates = account.getDateRange().split("/");
        int count=0;
        for(String date:dates)
        {
            if(count==0)
            {
                firstOldDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
                count+=1;
            }
            else
            {
                lastOldDate = LocalDate.parse(date,DateTimeFormatter.ISO_DATE);
            }
        }
        if(firstNewDate.isBefore(firstOldDate))
        {
            String newRange = firstNewDate.toString()+"/"+lastOldDate.toString();
            account.setDateRange(newRange);
            account.persistOrUpdate();
        }
        }
    }
}
