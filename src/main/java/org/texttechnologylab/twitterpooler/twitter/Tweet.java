package org.texttechnologylab.twitterpooler.twitter;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
/**
 * Tweet
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * This class provides tweet and all its attributes.
 **/
@MongoEntity(clientName="tweets",database = "Tweets" ,collection = "Tweets")
public class Tweet extends PanacheMongoEntity {

    String queryId;
    String conversation_id;
    ArrayList<JSONObject> referenced_tweets;
    Document entities;
    Boolean possibly_sensitive;
    Document public_metrics;
    String created_at;
    String source;
    String text;
    String author_id;
    String lang;
    String reply_settings;
    ArrayList<Document> users;
    ArrayList<Document> tweets;
    ArrayList<Document> media;
    ArrayList<Document> places;
    ArrayList<Document> polls;
    String object_id;
    String casObject;

    public String getCasObject() {
        return casObject;
    }

    public void setCasObject(String casObject) {
        this.casObject = casObject;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
    }

    public ArrayList<JSONObject> getReferenced_tweets() {
        return referenced_tweets;
    }

    public void setReferenced_tweets(ArrayList<JSONObject> referenced_tweets) {
        this.referenced_tweets = referenced_tweets;
    }

    public Document getEntities() {
        return entities;
    }

    public void setEntities(Document entities) {
        this.entities = entities;
    }

    public Boolean getPossibly_sensitive() {
        return possibly_sensitive;
    }

    public void setPossibly_sensitive(Boolean possibly_sensitive) {
        this.possibly_sensitive = possibly_sensitive;
    }

    public Document getPublic_metrics() {
        return public_metrics;
    }

    public void setPublic_metrics(Document public_metrics) {
        this.public_metrics = public_metrics;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getReply_settings() {
        return reply_settings;
    }

    public void setReply_settings(String reply_settings) {
        this.reply_settings = reply_settings;
    }

    public ArrayList<Document> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Document> users) {
        this.users = users;
    }

    public ArrayList<Document> getTweets() {
        return tweets;
    }

    public void setTweets(ArrayList<Document> tweets) {
        this.tweets = tweets;
    }

    public ArrayList<Document> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<Document> media) {
        this.media = media;
    }

    public ArrayList<Document> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<Document> places) {
        this.places = places;
    }

    public ArrayList<Document> getPolls() {
        return polls;
    }

    public void setPolls(ArrayList<Document> polls) {
        this.polls = polls;
    }

    /**
     * Create tweet from MongoDB Document
     * @param queryId
     * @param conversation_id
     * @param referenced_tweets
     * @param entities
     * @param possibly_sensitive
     * @param public_metrics
     * @param created_at
     * @param source
     * @param text
     * @param author_id
     * @param lang
     * @param reply_settings
     * @param users
     * @param tweets
     * @param media
     * @param places
     * @param polls
     * @param object_id
     * @param casObject
     */
    @BsonCreator
    public Tweet(
            @BsonProperty("queryId") String queryId,
            @BsonProperty("conversation_id") String conversation_id,
            @BsonProperty("referenced_tweets")ArrayList<JSONObject> referenced_tweets,
            @BsonProperty("entities")Document entities,
            @BsonProperty("possibly_sensitive")Boolean possibly_sensitive,
            @BsonProperty("public_metrics")Document public_metrics,
            @BsonProperty("created_at")String created_at,
            @BsonProperty("source")String source,
            @BsonProperty("text")String text,
            @BsonProperty("author_id")String author_id,
            @BsonProperty("lang")String lang,
            @BsonProperty("reply_settings")String reply_settings,
            @BsonProperty("users")ArrayList<Document> users,
            @BsonProperty("tweets")ArrayList<Document> tweets,
            @BsonProperty("media")ArrayList<Document> media,
            @BsonProperty("places")ArrayList<Document> places,
            @BsonProperty("polls")ArrayList<Document> polls,
            @BsonProperty("object_id")String object_id,
            @BsonProperty("casObject")String casObject

            ) {
        this.queryId = queryId;
        this.conversation_id = conversation_id;
        this.referenced_tweets = referenced_tweets;
        this.created_at = created_at;
        this.entities = entities;
        this.author_id = author_id;
        this.lang = lang;
        this.media = media;
        this.places = places;
        this.polls = polls;
        this.possibly_sensitive = possibly_sensitive;
        this.reply_settings = reply_settings;
        this.source = source;
        this.text = text;
        this.users = users;
        this.tweets = tweets;
        this.public_metrics = public_metrics;
        this.object_id = object_id;
        this.casObject = casObject;
    }

    /**
     * Get Tweet by tweetId
     * @param queryId queryId
     * @param id tweetId
     * @param mongoClientTweets Database
     * @return Tweet
     */
    public static Tweet getTweetById(String queryId, String id, MongoClient mongoClientTweets)
    {
        MongoDatabase db =  mongoClientTweets.getDatabase("Tweets");
        MongoCollection<Tweet> tes = db.getCollection(queryId,Tweet.class);
        var result = tes.find(eq("object_id",id)).into(new ArrayList<>());
        return result.get(0);
    }

    /**
     * Get all tweets from query
     * @param queryId queryId
     * @param mongoClientTweets mongo client
     * @return ArrayList<Tweet>
     */
    public static ArrayList<Tweet> getTweetsByQueryId(String queryId, MongoClient mongoClientTweets)
    {
        MongoDatabase db =  mongoClientTweets.getDatabase("Tweets");
        MongoCollection<Tweet> tes = db.getCollection(queryId,Tweet.class);
        var result = tes.find(eq("queryId",queryId)).into(new ArrayList<>());
        return result;
    }

    /**
     * Get all tweets from pool
     * @param pool poll name
     * @param mongoClientTweets mongo client
     * @return
     */
    public static ArrayList<Tweet> getTweetsByPool(String pool, MongoClient mongoClientTweets)
    {
        MongoDatabase db =  mongoClientTweets.getDatabase("Pools");
        MongoCollection<Tweet> tes = db.getCollection(pool,Tweet.class);
        var result = tes.find().into(new ArrayList<>());
        return result;
    }
    /**
     * Get all tweets from query
     * @param queryId queryId
     * @param mongoClientTweets mongo database
     * @return ArrayList<Tweet>
     */
    public static ArrayList<Tweet> getTweetsByQueryIdWithMongoDb(String queryId, MongoDatabase mongoClientTweets)
    {
        //MongoDatabase db =  mongoClientTweets.getDatabase("Tweets");
        MongoCollection<Tweet> tes = mongoClientTweets.getCollection(queryId,Tweet.class);
        var result = tes.find(eq("queryId",queryId)).into(new ArrayList<>());
        return result;
    }

    /**
     * Update tweet JCas
     * @param tweet Tweet
     * @param mongoClient database
     */
    public static void updateTweetJCas(Tweet tweet, MongoDatabase mongoClient)
    {
        MongoCollection<Document> collection = mongoClient.getCollection(tweet.getQueryId());
        Document found = (Document)collection.find(eq("object_id",tweet.getObject_id())).first();

        Bson updatedValue = new Document("casObject", tweet.getCasObject());
        Bson updateOp = new Document("$set", updatedValue);
        collection.updateOne(found, updateOp);
    }

    /**
     * Mark tweet
     * @param tweet Tweet
     * @param mongoClient database
     */
    public static void updateTweetSource(Tweet tweet, MongoDatabase mongoClient)
    {
        MongoCollection<Document> collection = mongoClient.getCollection(tweet.getQueryId());
        Document found = (Document)collection.find(eq("object_id",tweet.getObject_id())).first();

        Bson updatedValue = new Document("source", "Textimager");
        Bson updateOp = new Document("$set", updatedValue);
        collection.updateOne(found, updateOp);
    }

}
