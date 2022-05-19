package twitter;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.mongodb.panache.common.MongoEntity;

import java.util.List;
/**
 * QueryAccount
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * This class provides query account and all its attributes.
 **/
@MongoEntity(clientName="test",database = "Administration" ,collection = "Query")
public class QueryAccount extends PanacheMongoEntity {

    String queryId="";
    String user = "";
    String nextToken="";
    String query="";
    String endpoint="";
    String createdAt;
    String startTime="";
    String endTime="";
    String dateRange="";
    int allTweets= 0;
    int tweetCount=0;
    String status="";
    String STools="";
    String multi="";
    String one="";

    public String getMulti() {
        return multi;
    }

    public void setMulti(String multi) {
        this.multi = multi;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getSTools() {
        return STools;
    }

    public void setSTools(String STools) {
        this.STools = STools;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getRecent() {
        return recent;
    }

    public void setRecent(Boolean recent) {
        this.recent = recent;
    }

    Boolean recent = true;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public QueryAccount(){}
    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getAllTweets() {
        return allTweets;
    }

    public void setAllTweets(int allTweets) {
        this.allTweets = allTweets;
    }

    public int getTweetCount() {
        return tweetCount;
    }

    public void setTweetCount(int tweetCount) {
        this.tweetCount = tweetCount;
    }

    /**
     * Get all accounts by query's
     * @param query query
     * @return List<QueryAccount>
     */
    public static List<QueryAccount> getQueryAccountsByQuery(String query)
    {
        PanacheQuery accCheck = QueryAccount.find("query",query);
        return accCheck.list();
    }

    /**
     * Get query account
     * @param queryId
     * @return QueryAccount
     */
    public static QueryAccount getQueryAccount(String queryId)
    {
        PanacheQuery<QueryAccount> check = QueryAccount.find("queryId", queryId);
        List<QueryAccount> accounts = check.list();
        return accounts.get(0);
    }

    /**
     * Create query account
     * @param queryId queryId
     * @param query search query
     * @param nextToken next token
     * @param user user
     * @param allTweets all tweets to download
     * @param tweetCount downloaded tweets
     * @param endpoint requested endpoint
     * @param created_at created at
     * @param startTime date of first tweet
     * @param endTime date of last tweet
     * @param status job status
     * @param STools nlp tools
     */
    public static void createQueryAccount(String queryId, String query, String nextToken, String user, int allTweets, int tweetCount, String endpoint,
                                          String created_at, String startTime, String endTime, String status, String STools)
    {
        QueryAccount queryAcc = new QueryAccount();
        queryAcc.setQueryId(queryId);
        queryAcc.setQuery(query);
        queryAcc.setNextToken(nextToken);
        queryAcc.setUser(user);
        queryAcc.setAllTweets(allTweets);
        queryAcc.setTweetCount(tweetCount);
        queryAcc.setEndpoint(endpoint);
        queryAcc.setStartTime(startTime);
        queryAcc.setEndTime(endTime);
        queryAcc.setStatus(status);
        queryAcc.setCreatedAt(created_at);
        queryAcc.setSTools(STools);
        queryAcc.setOne("no available");
        queryAcc.setMulti("no available");
        //save
        queryAcc.persist();
    }

    /**
     * Update number of downloaded tweets
     * @param queryId queryId
     * @param count number of tweets to update
     */
    public static void updateTweetCount(String queryId, int count)
    {
        QueryAccount acc = getQueryAccount(queryId);
        int oldCount = acc.getTweetCount();
        int newCount = oldCount+count;
        acc.setTweetCount(newCount);
        acc.update();
    }

    /**
     * Find number of downloaded tweets
     * @param queryId queryId
     * @return int
     */
    public static int findTweetCount(String queryId)
    {
        QueryAccount acc = getQueryAccount(queryId);
        return acc.getTweetCount();
    }

    /**
     * Set new next token
     * @param queryId queryId
     * @param nextToken next token
     */
    public static void updateNextToken(String queryId, String nextToken)
    {
        QueryAccount acc = getQueryAccount(queryId);
        acc.setNextToken(nextToken);
        acc.update();
    }

    /**
     * Update job status
     * @param queryId queryId
     * @param status status
     */
    public static void updateStatus(String queryId, String status)
    {
        QueryAccount acc = getQueryAccount(queryId);
        acc.setStatus(status);
        acc.update();
    }

    /**
     * Find next token with queryId
     * @param queryId queryId
     * @return nexttoken
     */
    public static String findNextToken(String queryId)
    {
        QueryAccount acc = getQueryAccount(queryId);
        return acc.getNextToken();
    }

    /**
     * check if job is done
     * @param queryId queryId
     * @return boolean
     */
    public static Boolean checkIfDone(String queryId)
    {
        QueryAccount acc = getQueryAccount(queryId);
        if(acc.getTweetCount()== acc.getAllTweets())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Find endpoint with queryId
     * @param queryId queryId
     * @return endpoint
     */
    public static String findEndpoint(String queryId)
    {
        QueryAccount acc = getQueryAccount(queryId);
        return acc.getEndpoint();
    }
}
