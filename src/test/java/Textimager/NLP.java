package Textimager;

import com.mongodb.client.MongoClient;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import io.quarkus.mongodb.MongoClientName;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.texttechnologylab.uimadb.wrapper.mongo.MongoSerialization;
import twitter.Tweet;


import javax.inject.Inject;
import java.util.ArrayList;


@QuarkusTest
public class NLP {
    @Inject
    @MongoClientName("tweets")
    MongoClient mongoClientTweets;
    @Test
    public void nlpTest() throws  UIMAException {
        //String queryId = "1b03cf4a-956f-44e8-9f25-f354360d2011";
        //String id="1492506860406116354";
        //for parallel
        String queryId = "d2dc6f40-81da-4f50-aca0-23fe6d9b4a77";
        String id="1494332398468624392";
        Tweet tweet = Tweet.getTweetById(queryId, id,mongoClientTweets);
        var sd = tweet.getCasObject();
        JCas jCas = JCasFactory.createJCas();
        MongoSerialization.deserializeJCas(jCas, sd);
        var te = JCasUtil.select(jCas, Token.class);
        String firstTokenTweet = te.stream().findFirst().get().getCoveredText();
        //JSONObject jsonObject = new JSONObject(e);
        String firstTokenTest = "Es";
        //JCas cas = DBHelper.JsonToCas(jsonObject);
        Assertions.assertTrue(firstTokenTest.equals(firstTokenTweet));
    }
    @Test
    public void NLPSpacy() throws UIMAException {
        String queryId = "03f7ed15-c37e-4029-b151-2d7a75ac71b5";
        //String Tweetid ="1489301134342721539";
        ArrayList<Tweet> tweets = Tweet.getTweetsByQueryId(queryId, mongoClientTweets);
        int count = 0;
        for (Tweet tweet:tweets)
        {
            var sd = tweet.getCasObject();
            JCas jCas = JCasFactory.createJCas();
            MongoSerialization.deserializeJCas(jCas, sd);
            var te = JCasUtil.select(jCas, Token.class);
            if (te.size()>0){count = count +1;}
        }
        String test = "";
        Assertions.assertTrue(count == tweets.size());
    }
}
