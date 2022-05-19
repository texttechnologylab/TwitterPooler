package PoolingTest;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.quarkus.mongodb.MongoClientName;
import io.quarkus.test.junit.QuarkusTest;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tweets.PoolingStarterRunnable;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@QuarkusTest
public class AuthorPooling {


    @Inject
    @MongoClientName("tweets")
    MongoClient mongoClientTweets;

    @Test
    public void authorWiseTest()
    {
        MongoDatabase mongoClient = mongoClientTweets.getDatabase("Tweets");
        MongoCollection<Document> mongoCollection = mongoClient.getCollection("ebae3d8e-36d0-41e2-b87b-92ec10c8ebda");
        //MongoCollection<Document> mongoCollection = mongoClient.getCollection("20165d3b-f3c1-4b5e-a874-cc6fb8ad7bcd");


        Map<String, ArrayList<Document>> pools = new HashMap<String, ArrayList<Document>>();
        pools = PoolingStarterRunnable.startAuthorWisePooling(mongoCollection);

        for (var entry : pools.entrySet()) {
            String authorId = entry.getKey();
            for(int i = 0; i<entry.getValue().size(); i++)
            {
                boolean check = false;
                var obj = entry.getValue().get(i);
                ArrayList<Object> list = (ArrayList<Object>) obj.get("users");
                for(int j = 0; j <list.size();j++)
                {
                    String tempId = (((Document) list.get(0)).get("id")).toString();
                    if (tempId.equals(authorId))
                    {
                        check = true;
                    }
                }
                Assertions.assertTrue(check);
            }
        }

    }
}
