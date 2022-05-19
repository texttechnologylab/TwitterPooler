package databaseTests;

import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import twitter.TwitterAccount;

import java.util.List;

@QuarkusTest
public class TwitterAccountPanacheTest {
    @Test
    public void getTwitterAccountByName()
    {
        PanacheQuery<TwitterAccount> accountQuery = TwitterAccount.find("name", "Grzegorz");
        List<TwitterAccount> accounts = accountQuery.list();
        Assertions.assertTrue(accounts.get(0).getName().equals("Grzegorz"));
    }

    @Test
    public void getAllAccounts()
    {
        var accounts = TwitterAccount.findAll();
        Assertions.assertTrue(accounts.count() == 1);

    }
}
