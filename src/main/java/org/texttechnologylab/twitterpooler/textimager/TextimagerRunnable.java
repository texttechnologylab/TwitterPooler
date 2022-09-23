package org.texttechnologylab.twitterpooler.textimager;

import com.mongodb.client.MongoDatabase;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.hucompute.textimager.client.TextImagerClient;
import org.hucompute.textimager.uima.spacy.SpaCyMultiTagger3;
import org.texttechnologylab.uimadb.wrapper.mongo.MongoSerialization;
import org.texttechnologylab.twitterpooler.twitter.QueryAccount;
import org.texttechnologylab.twitterpooler.twitter.Tweet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

/**
 * TextimagerRunnable
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * Textimager job.
 **/
public class TextimagerRunnable implements Runnable{

    private MongoDatabase mongoClient;
    private TextImagerClient client;
    private String STools;
    private String queryId;

    public TextimagerRunnable(MongoDatabase mongoClient, TextImagerClient client, String STools, String queryId)
    {
        this.mongoClient = mongoClient;
        this.client = client;
        this.STools = STools;
        this.queryId = queryId;
    }

    /**
     * This function start the NLP process.
     */
    @Override
    public void run() {
        QueryAccount queryAccount = QueryAccount.getQueryAccount(queryId);
        if(queryAccount.getStatus().equals("Complete") && STools != null)
        {
            try {

            //collect all Tweets from query
            ArrayList<Tweet> tweets = Tweet.getTweetsByQueryIdWithMongoDb(queryId, mongoClient);
            AggregateBuilder pipeline = new AggregateBuilder();
            pipeline.add(createEngineDescription(SpaCyMultiTagger3.class,
                        SpaCyMultiTagger3.PARAM_REST_ENDPOINT, "http://spacy.prg2021.texttechnologylab.org"
                ));
            AnalysisEngine pAE = pipeline.createAggregate();
            int counter = 0;

            //parallel function
            //https://stackoverflow.com/questions/2016083/what-is-the-easiest-way-to-parallelize-a-task-in-java
            ExecutorService manager = Executors.newFixedThreadPool(4);
            List<Callable<Tweet>> tasks = new ArrayList<Callable<Tweet>>();
            for(Tweet tweet: tweets)
            {
                int finalCounter = counter;
                Callable<Tweet> c = new Callable<Tweet>() {
                    final  int count = finalCounter;
                    @Override
                    public Tweet call() throws Exception {
                        var casString = tweet.getCasObject();
                        JCas jCas = null;

                        jCas = JCasFactory.createJCas();
                        MongoSerialization.deserializeJCas(jCas, casString);
                        //client.process(jCas.getCas(), STools.split(","));
                        SimplePipeline.runPipeline(jCas, pAE);

                        //notifyAll();
                            tweet.setCasObject(MongoSerialization.serializeJCas(jCas));

                        Tweet.updateTweetJCas(tweet,mongoClient);
                            System.out.println("Tweet jcas was updated! "+count);
                            Tweet.updateTweetSource(tweet, mongoClient);
                            tweets.remove(tweet);
                        return tweet;

                }};
                counter = counter+1;
                tasks.add(c);
            }
            List<Future<Tweet>> results = manager.invokeAll(tasks);

            //do nlp old not parallel
            /*for (Tweet tweet : tweets)
            {
                var casString = tweet.getCasObject();
                JCas jCas = JCasFactory.createJCas();
                MongoSerialization.deserializeJCas(jCas, casString);
                //client.process(jCas.getCas(), STools.split(","));
                SimplePipeline.runPipeline(jCas, pAE);
                //notifyAll();
                tweet.setCasObject(MongoSerialization.serializeJCas(jCas));
                System.out.println("Procces succesfuly : " + casString);
                Tweet.updateTweetJCas(tweet,mongoClient);
                System.out.println("Tweet " +counter +" jcas was updated!");
                Tweet.updateTweetSource(tweet, mongoClient);
                counter = counter +1;
            }*/
            //persist
                /*for(int i=0; i<results.size();i++)
                {

                    Tweet.updateTweetJCas(results.get(i).get(),mongoClient);
                    System.out.println("Tweet jcas was updated!");
                    Tweet.updateTweetSource(results.get(i).get(), mongoClient);
                }*/
            /*for (Tweet te: newTweets)
            {
                Tweet.updateTweetJCas(te,mongoClient);
                System.out.println("Tweet jcas was updated!");
                Tweet.updateTweetSource(te, mongoClient);
            }*/
                System.out.println("Textimager job is done!");
            }
            catch (Exception e)
            {
                System.out.println("TextimagerRunnable : "+ e);
            }
            //client.process(cas.getCas(), STools.split(","));

        }

    }
}


