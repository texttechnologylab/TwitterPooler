package databaseTests;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import database.DBHelper;
import io.quarkus.mongodb.MongoClientName;
import io.quarkus.test.junit.QuarkusTest;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import twitter.Tweet;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import static com.mongodb.client.model.Filters.eq;

@QuarkusTest
public class TweetTest {

    @Inject
    @MongoClientName("tweets")
    MongoClient mongoClientTweets;

    @Test
    public void getTweetsByQueryId()
    {
        //String queryId = "af7ceb3a-7983-43cd-ba42-b6200b382bbc";
        //mac
        String queryId = "958e5bda-64b0-4d32-8195-20fa9abe3ce0";
        ArrayList<Tweet> tweets = Tweet.getTweetsByQueryId(queryId,mongoClientTweets);

        MongoDatabase db =  mongoClientTweets.getDatabase("Tweets");
        MongoCollection<Tweet> test = db.getCollection(queryId,Tweet.class);

        Assertions.assertTrue(tweets.size() == test.countDocuments());
    }

    @Test
    public void getTweetbyIdTest()
    {
        //String queryId = "af7ceb3a-7983-43cd-ba42-b6200b382bbc";
        //String id = "1468982766427676676";

        //mac
        String queryId = "958e5bda-64b0-4d32-8195-20fa9abe3ce0";
        String id="1469055830855233539";
        Tweet tweet = Tweet.getTweetById(queryId, id,mongoClientTweets);

        Assertions.assertTrue(tweet.getObject_id().equals(id));

    }

    @Test
    public void tweetUpdate()
    {
        /*String queryId="b39a0fdd-0ab0-4cf1-9d70-2de2864a1b6b";
        String tweetId = "1469725496766353416";

        MongoDatabase  tes = mongoClientTweets.getDatabase("Tweets");
        MongoCollection<Document> collection = tes.getCollection(queryId);
        Document found = (Document)collection.find(eq("object_id",tweetId)).first();

        Bson updatedValue = new Document("source", "test");
        Bson updateOp = new Document("$set", updatedValue);
        collection.updateOne(found, updateOp);*/
    }

@Test
    public void cleantext()
    {
        String queryId = "958e5bda-64b0-4d32-8195-20fa9abe3ce0";
        String id="1469055830855233539";
        Tweet tweet = Tweet.getTweetById(queryId, id,mongoClientTweets);
        String orgText = tweet.getText();
        String preText = DBHelper.cleanText(orgText);


        LocalDateTime inputDate = LocalDateTime.parse(tweet.getCreated_at(), DateTimeFormatter.ISO_DATE_TIME);
        long test = (inputDate.getLong(ChronoField.EPOCH_DAY) * 86400000) + inputDate.getLong(ChronoField.MILLI_OF_DAY);

        String stop = "";
    }
}
