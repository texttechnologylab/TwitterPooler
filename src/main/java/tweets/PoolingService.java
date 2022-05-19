package tweets;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




/**
 * PoolingService
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * This class executes pooling jobs.
 **/
public class PoolingService {
    @Inject
    PoolingStarterRunnable poolingStarterRunnable;

    /**
     * Thread for pooling job.
     */
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    /**
     * This function launch new pooling process in a separate thread.
     * @param mongoClient MongoDatabase
     * @param mongoCollection MongoCollection<Docuemnt>
     * @param poolingArt describe which pooling art should be used
     * @param id queryId
     * @param poolValues pool size and pool limit values
     */
    public void start(MongoDatabase mongoClient, MongoCollection<Document> mongoCollection, String poolingArt, String id, String topics , Map<String,Integer>poolValues)
    {

        Runnable r = new PoolingStarterRunnable(mongoClient,mongoCollection, poolingArt, id, topics, poolValues);
        threadPool.submit(r);
    }
}
