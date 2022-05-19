package tweets;

import com.mongodb.client.MongoDatabase;
import org.hucompute.textimager.client.TextImagerClient;
import textimager.ClientTextimager;
import textimager.TextimagerRunnable;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * RequestService
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * This class executes request's and Textimager client.
 **/
public class RequestService {
    @Inject
    RequestSenderRunnable requestSenderRunnable;


    //private ExecutorService threadPool = Executors.newCachedThreadPool();
    /**
     * Thread for request and Textimager process.
     */
    private ExecutorService threadPool = Executors.newSingleThreadExecutor();

    /**
     * Start the job.
     * @param accountName twitter dev account
     * @param query query
     * @param mongoClient database
     * @param client Textimager client
     * @param STools nlp tools
     */
    public void start(String accountName, Query query, MongoDatabase mongoClient, TextImagerClient client, String STools)
    {

        Runnable r = new RequestSenderRunnable(accountName, query, mongoClient, client, STools);
        Runnable m = new TextimagerRunnable(mongoClient,client,STools,query.getUniqueId());

        threadPool.execute(r);
        threadPool.execute(m);


        //das
        /*List<Future<?>> futures = new ArrayList<>();
        List<Runnable> taskList = new ArrayList<>();
        taskList.add(r);
        for(Runnable task : taskList) {
            futures.add(threadPool.submit(task));
        }
        //threadPool.invokeAll(futures);

        for(Future<?> future : futures) {
            try {
                future.get();
            }catch(Exception e){
               // do logging and nothing else
                //System.out.println(future.get().toString());
                System.out.println(e);
            }
        }
        //threadPool.shutdown();

        taskList.remove(r);
        taskList.add(m);
        for(Runnable task : taskList) {
            futures.add(threadPool.submit(task));
        }
        //threadPool.invokeAll(futures);
        futures.remove(0);
        for(Future<?> future : futures) {
            try {
                future.get();
            }catch(Exception e){
                // do logging and nothing else
                //System.out.println(future.get().toString());
                System.out.println(e);
            }
        }
        threadPool.shutdown();
*/

    }
}
