package databaseTests;

import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.texttechnologylab.twitterpooler.twitter.QueryAccount;

import java.time.Instant;
import java.util.List;

@QuarkusTest
public class QueryAccountPanacheTest {


//    @Test
    public  void createQueryAccount()
    {
        //time
        Instant instant = Instant.now();

        QueryAccount.createQueryAccount("QueryID","Das ist Query", "","Grzegorz", 2,1,"endpoint",
                instant.toString(),instant.toString(),instant.toString(),"test","");
        //find query
        PanacheQuery<QueryAccount> check = QueryAccount.find("queryId", "QueryID");
        List<QueryAccount> accounts = check.list();
        Assertions.assertTrue(accounts.get(0).getQueryId().equals("QueryID"));
    }

//    @Test
    public void updateTweetCount()
    {
        int countToUpdate = 2;
        int oldCount = QueryAccount.findTweetCount("QueryID");
        QueryAccount.updateTweetCount("QueryID",countToUpdate);
        Assertions.assertTrue(QueryAccount.findTweetCount("QueryID") == countToUpdate+oldCount);
    }

//    @Test
    public void updateNextToken()
    {
        String oldToken = QueryAccount.findNextToken("QueryID");
        String generatedString = RandomStringUtils.randomAlphabetic(6);
        QueryAccount.updateNextToken("QueryID",generatedString);
        Assertions.assertTrue(!oldToken.equals(QueryAccount.findNextToken("QueryID")));
    }

//    @Test
    public void checkIfDone()
    {
        Assertions.assertTrue(QueryAccount.checkIfDone("QueryID"));
    }
}
