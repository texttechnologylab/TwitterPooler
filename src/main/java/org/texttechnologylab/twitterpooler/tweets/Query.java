package org.texttechnologylab.twitterpooler.tweets;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Query
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * This class provides requested query and all its attributes.
 **/
public class Query {

    String userFields = "created_at,description,entities,id,location,name,pinned_tweet_id,profile_image_url,protected,public_metrics,url,username,verified,withheld";
    String expansions = "attachments.poll_ids,attachments.media_keys,author_id,geo.place_id,in_reply_to_user_id,referenced_tweets.id,entities.mentions.username,referenced_tweets.id.author_id";
    String tweetFields = "attachments,author_id,context_annotations,conversation_id,created_at,entities,geo,id,in_reply_to_user_id,lang,possibly_sensitive,public_metrics,referenced_tweets,reply_settings,source,text,withheld";
    String placeFields = "contained_within,country,country_code,full_name,geo,id,name,place_type";
    String mediaFields = "duration_ms,height,media_key,preview_image_url,public_metrics,type,url,width";
    String poolFields = "duration_minutes,end_datetime,id,options,voting_status";
    String pinnedTweetId = "pinned_tweet_id";
    public JSONArray getUsers() {
        return users;
    }

    public void setUsers(JSONArray users) {
        this.users = users;
    }

    public JSONArray getTweets() {
        return tweets;
    }

    public void setTweets(JSONArray tweets) {
        this.tweets = tweets;
    }

    public JSONArray getPlaces() {
        return places;
    }

    public void setPlaces(JSONArray places) {
        this.places = places;
    }

    public JSONArray getMedia() {
        return media;
    }

    public void setMedia(JSONArray media) {
        this.media = media;
    }

    public JSONArray getPoll() {
        return poll;
    }

    public void setPoll(JSONArray poll) {
        this.poll = poll;
    }

    JSONArray users = new JSONArray();
    JSONArray tweets = new JSONArray();
    JSONArray places = new JSONArray();
    JSONArray media = new JSONArray();
    JSONArray poll = new JSONArray();

    URIBuilder uriBuilder;

    String Query;
    String TweetId;
    String UserId;
    String UserName;
    String DDCCategory;
    String Hashtag;
    String Place;
    String Keywords;
    String TweetTimeline;
    String MentionTimeline;
    int TweetCount;
    String MaxResults;
    String NextToken;
    String EndTime;
    String StartTime;
    String uniqueId;
    String endpoint;
    String STools;

    public String getSTools() {
        return STools;
    }

    public void setSTools(String STools) {
        this.STools = STools;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    boolean multiple = false;

    public String getEndpointValue() {
        return EndpointValue;
    }

    public void setEndpointValue(String endpointValue) {
        EndpointValue = endpointValue;
    }

    String EndpointValue;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getQuery() {
        return Query;
    }

    /**
     * Set request query
     * @param query query
     */
    public void setQuery(String query)
    {
        /*query = query.replace(" ","%20");
        query = query.replace("\"","%22");
        Query = query.replace("#","%23");*/
        Query = query;
    }

    public String getTweetId() {
        return TweetId;
    }

    public void setTweetId(String tweetId) {
        TweetId = tweetId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getDDCCategory() {
        return DDCCategory;
    }

    public void setDDCCategory(String DDCCategory) {
        this.DDCCategory = DDCCategory;
    }

    public String getHashtag() {
        return Hashtag;
    }

    public void setHashtag(String hashtag)
    {
        /*hashtag = hashtag.replace(" ","%20");
        Hashtag = hashtag.replace("#","%23");*/
        Hashtag = hashtag;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getKeywords() {
        return Keywords;
    }

    public void setKeywords(String keywords)
    {
        /*keywords = keywords.replace(" ","%20");
        keywords = keywords.replace("\"","%22");
        Keywords = keywords.replace("#","%23");*/
        Keywords = keywords;
    }

    public String getTweetTimeline() {
        return TweetTimeline;
    }

    public void setTweetTimeline(String tweetTimeline) {
        TweetTimeline = tweetTimeline;
    }

    public String getMentionTimeline() {
        return MentionTimeline;
    }

    public void setMentionTimeline(String mentionTimeline) {
        MentionTimeline = mentionTimeline;
    }

    public int getTweetCount() {
        return TweetCount;
    }

    public void setTweetCount(int tweetCount) {
        TweetCount = tweetCount;
    }

    public String getMaxResults() {
        return MaxResults;
    }

    public void setMaxResults(String maxResults) {
        MaxResults = maxResults;
    }

    public String getNextToken() {
        return NextToken;
    }

    public void setNextToken(String nextToken) {
        NextToken = nextToken;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }


    /**
     * Format date string to compatible format
     * @param date
     * @return
     */
    public String dateStringFormatting(String date)
    {
        date = date.replace(" ","T");
        date = date +"Z";
        //YYYY-MM-DDTHH:mm:ssZ (ISO 8601/RFC 3339)
        return date;
    }

    /**
     * Create URI for search endpoint Query.
     * @param maxResults number of maximum requested tweets
     * @param nextToken nextToken
     * @param query query
     * @param startTime date of first tweet
     * @param endTime date of last tweet
     * @param endpoint requested endpoint
     * @return URIBuilder, ready to send
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     */
    public URIBuilder createSearchQuery(String maxResults, String nextToken, String query, String startTime, String endTime, String endpoint) throws URISyntaxException, UnsupportedEncodingException {

        //tutaj zmiana odnosnie url
        String uriQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
        uriBuilder = new URIBuilder(String.format(endpoint, uriQuery));
        //set parameters
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("user.fields", userFields));
        queryParameters.add(new BasicNameValuePair("expansions", expansions));
        queryParameters.add(new BasicNameValuePair("tweet.fields", tweetFields));
        queryParameters.add(new BasicNameValuePair("place.fields", placeFields));
        queryParameters.add(new BasicNameValuePair("media.fields", mediaFields));
        queryParameters.add(new BasicNameValuePair("poll.fields", poolFields));
        queryParameters.add(new BasicNameValuePair("max_results", checkMaxResult(maxResults)));
        if(nextToken != null)
        {
            queryParameters.add(new BasicNameValuePair("next_token", nextToken));
        }
        if(startTime != null)
        {
            queryParameters.add(new BasicNameValuePair("start_time", startTime));

        }
        if (endTime != null)
        {
            queryParameters.add(new BasicNameValuePair("end_time", endTime));

        }


        uriBuilder.addParameters(queryParameters);
        return uriBuilder;
    }

    /**
     * Create URI for timeline endpoint Query.
     * @param endpointName requested endpoint
     * @param maxResults max result
     * @param nextToken next token
     * @param userId Twitter userid
     * @param startTime date of first tweet
     * @param endTime date of last tweet
     * @return URIBuilder, ready to send
     * @throws URISyntaxException
     */
    public URIBuilder createTimelinesQuery(String endpointName, String maxResults, String nextToken, String userId, String startTime, String endTime) throws URISyntaxException {

        uriBuilder = new URIBuilder(String.format(endpointName, userId));
        //set parameters
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("user.fields", userFields));
        queryParameters.add(new BasicNameValuePair("expansions", expansions));
        queryParameters.add(new BasicNameValuePair("tweet.fields", tweetFields));
        queryParameters.add(new BasicNameValuePair("place.fields", placeFields));
        queryParameters.add(new BasicNameValuePair("media.fields", mediaFields));
        queryParameters.add(new BasicNameValuePair("poll.fields", poolFields));
        queryParameters.add(new BasicNameValuePair("max_results", checkMaxResult(maxResults)));
        if(nextToken != null)
        {
            //next_token
            queryParameters.add(new BasicNameValuePair("pagination_token", nextToken));
        }
        if(startTime != null)
        {
            queryParameters.add(new BasicNameValuePair("start_time", startTime));

        }
        if (endTime != null)
        {
            queryParameters.add(new BasicNameValuePair("end_time", endTime));

        }

        uriBuilder.addParameters(queryParameters);
        return uriBuilder;
    }

    /**
     * Create URI for count endpoint Query.
     * @param query query
     * @param startTime date of first tweet
     * @param endTime date of last tweet
     * @param endpoint requested endpoint
     * @return URIBuilder, ready to send
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     */
    public URIBuilder createCountQuery(String query, String startTime, String endTime, String endpoint) throws URISyntaxException, UnsupportedEncodingException {
        //tutaj zmiana odnosnie url
        String uriQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
        uriBuilder = new URIBuilder(String.format(endpoint, uriQuery));
        //set parameters
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        if(startTime != null)
        {
            queryParameters.add(new BasicNameValuePair("start_time", startTime));

        }
        if (endTime != null)
        {
            queryParameters.add(new BasicNameValuePair("end_time", endTime));

        }

        uriBuilder.addParameters(queryParameters);
        return uriBuilder;
    }

    /**
     * Create URI for tweet endpoint Query.
     * @param endpoint requested endpoint
     * @param tweetId requested tweetId/tweetId's
     * @return URIBuilder,ready to send
     * @throws URISyntaxException
     */
    public URIBuilder createTweetQuery(String endpoint, String tweetId) throws URISyntaxException {

        //check if multiple
        tweetId = checkIfMultpile(tweetId);

        uriBuilder = new URIBuilder(String.format(endpoint, tweetId));
        //set parameters
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("user.fields", userFields));
        queryParameters.add(new BasicNameValuePair("expansions", expansions));
        queryParameters.add(new BasicNameValuePair("tweet.fields", tweetFields));
        queryParameters.add(new BasicNameValuePair("place.fields", placeFields));
        queryParameters.add(new BasicNameValuePair("media.fields", mediaFields));
        queryParameters.add(new BasicNameValuePair("poll.fields", poolFields));

        uriBuilder.addParameters(queryParameters);
        return uriBuilder;
    }

    /**
     * Create URI for user endpoint query (deprecated)
     * @param endpointName requested endpoint
     * @param userId requested userId/usersId's
     * @return URIBuilder, ready to send
     * @throws URISyntaxException
     */
    public URIBuilder createUserQuery(String endpointName, String userId) throws URISyntaxException {

        //check if multiple
        userId = checkIfMultpile(userId);

        uriBuilder = new URIBuilder(String.format(endpointName, userId));
        //set parameters
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("user.fields", userFields));
        queryParameters.add(new BasicNameValuePair("expansions", pinnedTweetId));
        queryParameters.add(new BasicNameValuePair("tweet.fields", tweetFields));

        uriBuilder.addParameters(queryParameters);
        return uriBuilder;
    }

    /**
     * Create URI for followers endpoint query (deprecated)
     * @param endpointName requested endpoint
     * @param userId requested userId/userId's
     * @param maxResult max result
     * @return URIBuilder, ready to send
     * @throws URISyntaxException
     */
    public URIBuilder createFollowersQuery(String endpointName, String userId, String maxResult) throws URISyntaxException {

        uriBuilder = new URIBuilder(String.format(endpointName, userId));
        //set parameters
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("user.fields", userFields));
        queryParameters.add(new BasicNameValuePair("expansions", pinnedTweetId));
        queryParameters.add(new BasicNameValuePair("tweet.fields", tweetFields));
        queryParameters.add(new BasicNameValuePair("max_results", checkMaxResult(maxResult)));


        uriBuilder.addParameters(queryParameters);
        return uriBuilder;
    }

    /**
     * Create URI for following endpoint query (deprecated)
     * @param endpointName requested endpoint
     * @param userId requested userId/usersId's
     * @param maxResult max result
     * @return URIBuilder, ready to send
     * @throws URISyntaxException
     */
    public URIBuilder createFollowingQuery(String endpointName, String userId, String maxResult) throws URISyntaxException {

        uriBuilder = new URIBuilder(String.format(endpointName, userId));
        //set parameters
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("user.fields", userFields));
        queryParameters.add(new BasicNameValuePair("expansions", pinnedTweetId));
        queryParameters.add(new BasicNameValuePair("tweet.fields", tweetFields));
        queryParameters.add(new BasicNameValuePair("max_results", checkMaxResult(maxResult)));


        uriBuilder.addParameters(queryParameters);
        return uriBuilder;
    }

    /**
     * Create URI for liked endpoint query
     * @param endpointName requested endpoint
     * @param userId requested userId/usersId's
     * @param maxResult max result
     * @param nextToken next token
     * @return URIBuilder, ready to send
     * @throws URISyntaxException
     */
    public URIBuilder createLikedQuery(String endpointName, String userId, String maxResult, String nextToken) throws URISyntaxException {
        //String expansionss = "pinned_tweet_id";
        uriBuilder = new URIBuilder(String.format(endpointName, userId));
        //set parameters
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("user.fields", userFields));
        queryParameters.add(new BasicNameValuePair("expansions", expansions));
        queryParameters.add(new BasicNameValuePair("tweet.fields", tweetFields));
        queryParameters.add(new BasicNameValuePair("max_results", checkMaxResult(maxResult)));

        if(nextToken != null)
        {
            //next_token
            queryParameters.add(new BasicNameValuePair("pagination_token", nextToken));
        }

        uriBuilder.addParameters(queryParameters);
        return uriBuilder;
    }

    /**
     * Create URI for liking endpoint query
     * @param endpointName requested endpoint
     * @param tweetId requested tweetId
     * @return URIBuilder, ready to send
     * @throws URISyntaxException
     */
    public URIBuilder createLikingQuery(String endpointName, String tweetId) throws URISyntaxException {

        uriBuilder = new URIBuilder(String.format(endpointName, tweetId));
        //set parameters
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("user.fields", userFields));
        queryParameters.add(new BasicNameValuePair("expansions", pinnedTweetId));
        queryParameters.add(new BasicNameValuePair("tweet.fields", tweetFields));


        uriBuilder.addParameters(queryParameters);
        return uriBuilder;
    }

    /**
     * Create URI for retweeted endpoint query (deprecated)
     * @param endpointName requested endpoint
     * @param tweetId requested tweetId
     * @return URIBuilder, ready to send
     * @throws URISyntaxException
     */
    public URIBuilder createRetweetedQuery(String endpointName, String tweetId) throws URISyntaxException {

        uriBuilder = new URIBuilder(String.format(endpointName, tweetId));
        //set parameters
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("user.fields", userFields));
        queryParameters.add(new BasicNameValuePair("expansions", pinnedTweetId));
        queryParameters.add(new BasicNameValuePair("tweet.fields", tweetFields));


        uriBuilder.addParameters(queryParameters);
        return uriBuilder;
    }

    /**
     * Check user/tweetId if more than one id and return adapted query
     * @param tweetId query with id's
     * @return adapted query
     */
    private String checkIfMultpile(String tweetId)
    {
        String result;
        if(tweetId.contains(","))
        {
            result = "?ids="+tweetId;
        }
        else
        {
            result = "/"+tweetId;
        }
        return result;

    }

    /**
     * Set all includes from response
     * @param includes includes from response
     */
    public void setIncludesFields(JSONObject includes)
    {
        if (includes.opt("media") != null)
        {
            this.setMedia(includes.getJSONArray("media"));
        }
        if (includes.opt("users") != null)
        {
            this.setUsers(includes.getJSONArray("users"));
        }
        if (includes.opt("tweets") != null)
        {
            this.setTweets(includes.getJSONArray("tweets"));
        }
        if (includes.opt("polls") != null)
        {
            this.setPoll(includes.getJSONArray("polls"));
        }
        if (includes.opt("places") != null)
        {
            this.setPlaces(includes.getJSONArray("places"));
        }
    }

    /**
     * Check maxResult, because maximum for one request is 100.
     * @param maxResults max result
     * @return max result (100<=)
     */
    public String checkMaxResult(String maxResults)
    {
        if(maxResults!=null && !maxResults.equals("")){
        int maxtoInt = Integer.parseInt(maxResults);
        if (maxtoInt>100)
        {
            return "100";
        }
        else if(maxtoInt<10)
        {
            return "100";
        }
        else
        {
            return maxResults;
        }}
        return "100";
    }
}
