package database;

import de.tudarmstadt.ukp.dkpro.core.api.metadata.type.DocumentMetaData;
import org.apache.uima.UIMAException;
import org.apache.uima.cas.impl.XCASDeserializer;
import org.apache.uima.cas.impl.XCASSerializer;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.hucompute.textimager.client.TextImagerClient;
import org.json.JSONArray;
import org.json.JSONML;
import org.json.JSONObject;
import org.texttechnologylab.uimadb.wrapper.mongo.MongoSerialization;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * DBHelper
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * This class prepares tweets to be stored in the Mongodb database.
 **/
public class DBHelper{

    public DBHelper(){}

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    String queryId;

    /**
     * This function create MongoDB document from downloaded tweets.
     * @return Document
     */
    public static Document createTweetDocument(JSONObject result, int i, String queryId,
                                                JSONArray media, JSONArray tweets, JSONArray users, JSONArray poll, JSONArray place, TextImagerClient client, String STools) throws Throwable, java.lang.Exception {
        Document mongoDoc = new Document();
            mongoDoc.put("queryId", queryId);
            //add tweet data
            JSONObject tweetData = (JSONObject) ((JSONArray) result.get("data")).get(i);
            Iterator x = tweetData.keys();
            while (x.hasNext()) {
                String key = (String) x.next();
                //type check
                if (tweetData.get(key).getClass().getName().equals("org.json.JSONObject")) {
                    JSONObject test = tweetData.getJSONObject(key);
                    mongoDoc.put(key, test.toMap());

                } else if (tweetData.get(key).getClass().getName().equals("org.json.JSONArray")) {
                    mongoDoc.put(key, ((JSONArray) tweetData.get(key)).toList());
                } else {
                    if (key.equals("id")) {
                        mongoDoc.put("object_id", tweetData.get(key));
                    } else {
                        mongoDoc.put(key, tweetData.get(key));
                    }
                }
            }
            //----------- add tweet includes
            //---add users
            if (users.length() > 0) {
                JSONArray tempUsers = new JSONArray();
                tempUsers = DBHelper.mapUsersToTweetData(users, tweetData);
                if (tempUsers.length() > 0) {
                    mongoDoc.put("users", tempUsers.toList());
                }
            }
            //---add tweets
            if (tweets.length() > 0) {
                JSONArray tempTweets = new JSONArray();
                tempTweets = DBHelper.mapTweetsToTweetData(tweets, tweetData);
                if (tempTweets.length() > 0) {
                    mongoDoc.put("tweets", tempTweets.toList());
                }

            }
            //---add media
            if (media.length() > 0) {
                JSONArray tempMedia = new JSONArray();
                tempMedia = DBHelper.mapMediaToTweetData(media, tweetData);
                if (tempMedia.length() > 0) {
                    mongoDoc.put("media", tempMedia.toList());
                }

            }
            //---add poll
            if (poll.length() > 0) {
                JSONArray tempPolls = new JSONArray();
                tempPolls = DBHelper.mapPollsToTweetData(poll, tweetData);
                if (tempPolls.length() > 0) {
                    mongoDoc.put("polls", tempPolls.toList());
                }

            }
            //---add place
            if (place.length() > 0) {
                JSONArray tempPalces = new JSONArray();
                tempPalces = DBHelper.mapPlacesToTweetData(place, tweetData);
                if (tempPalces.length() > 0) {
                    mongoDoc.put("places", tempPalces.toList());
                }

            }
            ObjectId id = new ObjectId();
            mongoDoc.put("_id", id);
            //create CAS
            JCas cas = toCas(mongoDoc, id);
            //client.process(cas.getCas(), STools.split(","));
            //client.wait();
            var resultCas = MongoSerialization.serializeJCas(cas);

            //var resultCas = DBHelper.CasToJson(cas);
            //var resultCas = CasToJson(cas);
            mongoDoc.put("casObject", resultCas);
        return mongoDoc;
    }

    /**
     * This function map users from include with the relevant tweets.
     * @return Document
     */
    public static JSONArray mapUsersToTweetData(JSONArray users, JSONObject tweetData)
    {
        JSONArray tempUsers = new JSONArray();

        for(int j=0 ; j< users.length(); j++)
        {
            //data from includes
            JSONObject user = users.getJSONObject(j);
            String userId = (String)user.get("id");

            //data from tweetdata
            String authorId = (String)tweetData.opt("author_id");
            String replyToUserID = (String)tweetData.opt("in_reply_to_user_id");
            JSONArray mentions = tweetData.optJSONObject("entities").optJSONArray("mentions");
            JSONArray referencedTweets = tweetData.optJSONArray("referenced_tweets");


            if(authorId != null)
            {
                if(authorId.equals(userId))
                {
                    tempUsers.put(user);
                }
            }
            else if (replyToUserID !=null)
            {
                if(replyToUserID.equals(userId))
                {
                    tempUsers.put(user);
                }
            }
            else if (mentions != null)
            {
                for (int k = 0; k< mentions.length();k++)
                {
                    if(mentions.getJSONObject(k).opt("id").equals(userId))
                    {
                        tempUsers.put(user);
                    }
                }
            }
            else if(referencedTweets != null)
            {
                for (int m=0; m< referencedTweets.length();m++)
                {
                    if (referencedTweets.optJSONObject(m).opt("author_id")!=null)
                    {
                        if (referencedTweets.optJSONObject(m).opt("author_id").equals(userId))
                        {
                            tempUsers.put(user);
                        }
                    }
                }
            }

        }
        return tempUsers;
    }

    /**
     * This function map referenced tweets from include with the relevant tweets.
     * @return Document
     */
    public static JSONArray mapTweetsToTweetData(JSONArray tweets, JSONObject tweetData)
    {
        JSONArray tempTweets = new JSONArray();

        for(int j=0 ; j< tweets.length(); j++)
        {
            //data from includes
            JSONObject tweet = tweets.getJSONObject(j);
            String tweetId = (String)tweet.get("id");

            //data from tweetdata
            JSONArray referencedTweets = tweetData.optJSONArray("referenced_tweets");


            if(referencedTweets != null)
            {
                for (int m=0; m< referencedTweets.length();m++)
                {
                    if (referencedTweets.optJSONObject(m).opt("id")!=null)
                    {
                        if (referencedTweets.optJSONObject(m).opt("id").equals(tweetId))
                        {
                            tempTweets.put(tweet);
                        }
                    }
                }
            }
            else if (tweetData.opt("pinned_tweet_id") != null)
            {
                if(tweetData.opt("pinned_tweet_id").equals(tweetId))
                {
                    tempTweets.put(tweet);
                }
            }

        }
        return tempTweets;
    }
    /**
     * This function map pools from include with the relevant tweets.
     * @return Document
     */
    public static JSONArray mapPollsToTweetData(JSONArray polls, JSONObject tweetData)
    {
        JSONArray tempPolls = new JSONArray();

        for(int j=0 ; j< polls.length(); j++)
        {
            //data from includes
            JSONObject poll = polls.getJSONObject(j);
            String pollId = (String)poll.get("id");

            //data from tweetdata
            JSONObject attachments = tweetData.optJSONObject("attachments");


            if(attachments != null)
            {
                JSONArray pollIds = attachments.optJSONArray("poll_ids");
                if(pollIds!=null){
                for (int m=0; m< pollIds.length();m++)
                {
                    if(pollIds.get(m).equals(pollId))
                    {
                        tempPolls.put(poll);
                    }
                }}
            }
        }
        return tempPolls;
    }
    /**
     * This function map media from include with the relevant tweets.
     * @return Document
     */
    public static JSONArray mapMediaToTweetData(JSONArray media, JSONObject tweetData)
    {
        JSONArray tempPolls = new JSONArray();

        for(int j=0 ; j< media.length(); j++)
        {
            //data from includes
            JSONObject mediaIncl = media.getJSONObject(j);
            String mediaKey = (String)mediaIncl.get("media_key");

            //data from tweetdata
            JSONObject attachments = tweetData.optJSONObject("attachments");


            if(attachments != null)
            {
                JSONArray mediaKeys = attachments.optJSONArray("media_keys");
                if(mediaKeys!=null){
                for (int m=0; m< mediaKeys.length();m++)
                {
                    if(mediaKeys.get(m).equals(mediaKey))
                    {
                        tempPolls.put(mediaIncl);
                    }
                }}
            }

        }
        return tempPolls;
    }
    /**
     * This function map places from include with the relevant tweets.
     * @return Document
     */
    public static JSONArray mapPlacesToTweetData(JSONArray place, JSONObject tweetData)
    {
        JSONArray tempPlaces = new JSONArray();

        for(int j=0 ; j< place.length(); j++)
        {
            //data from includes
            JSONObject placeIncl = place.getJSONObject(j);
            String placeId = (String)placeIncl.get("id");

            //data from tweetdata
            JSONObject geo = tweetData.optJSONObject("geo");

            if(geo != null)
            {
                String geoID = (String)geo.get("place_id");
                    if(geoID.equals(placeId))
                    {
                        tempPlaces.put(placeIncl);
                    }

            }

        }
        return tempPlaces;
    }
    /**
     * This function create JCas to relevant tweet.
     * @return JCas
     */
    public static JCas toCas(Document tweet, ObjectId id)
    {
        String originalText = tweet.getString("text");
        String cleanText = cleanText(originalText);

        try {
            JCas pCas = JCasFactory.createText(cleanText, tweet.getString("lang"));

            org.texttechnologylab.annotation.twitter.Tweet tweetTextimager = new org.texttechnologylab.annotation.twitter.Tweet(pCas);
            tweetTextimager.setTwitterID(Long.parseLong(tweet.getString("object_id")));
            tweetTextimager.setUserId(Long.parseLong(tweet.getString("author_id")));
            tweetTextimager.setOriginalText(tweet.getString("text"));
            LocalDateTime inputDate = LocalDateTime.parse(tweet.getString("created_at"), DateTimeFormatter.ISO_DATE_TIME);
            long longDate = (inputDate.getLong(ChronoField.EPOCH_DAY) * 86400000) + inputDate.getLong(ChronoField.MILLI_OF_DAY);
            tweetTextimager.setCreate(longDate);
            tweetTextimager.setLanguage(tweet.getString("lang"));
            // jetzt ist es Teil des pCas
            tweetTextimager.addToIndexes();

            DocumentMetaData dmd = DocumentMetaData.create(pCas);
            dmd.setDocumentTitle(""+tweetTextimager.getTwitterID());
            dmd.setDocumentId(id.toString());
            dmd.addToIndexes();
            //System.out.println(XmlFormatter.getPrettyString(pCas.getCas()));
            return pCas;

        } catch (UIMAException e) {
            e.printStackTrace();
        }

        return null;
    }
    /**
     * This function clean text from inappropriate characters.
     * @return Document
     */
    public static String cleanText(String text)
    {
        Pattern characterFilter = Pattern.compile("[^\\x{00}-\\x{024F}]");
        String result = characterFilter.matcher(text).replaceAll("");

        return result;
    }

}
