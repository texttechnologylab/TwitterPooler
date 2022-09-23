package org.texttechnologylab.twitterpooler.twitter;

import com.mongodb.client.MongoClient;
import io.quarkus.mongodb.MongoClientName;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.mongodb.panache.common.MongoEntity;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
/**
 * TwitterAccount
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * This class provides twitter account and all its attributes.
 **/
@MongoEntity(clientName="test",database = "Administration" ,collection = "Accounts")
public class TwitterAccount extends PanacheMongoEntity {

    @Inject
    @MongoClientName("admin")
    MongoClient mongoClientAdmin;


    String name;
    String bearerToken;
    int twitterCap;
    String type;
    int twitterLimit;
    LocalDateTime reset;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTwitterLimit() {
        return twitterLimit;
    }

    public void setTwitterLimit(int twitterLimit) {
        this.twitterLimit = twitterLimit;
    }

    public LocalDateTime getReset() {
        return reset;
    }

    public void setReset(LocalDateTime reset) {
        this.reset = reset;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    public int getTwitterCap() {
        return twitterCap;
    }

    public void setTwitterCap(int twitterCap) {
        this.twitterCap = twitterCap;
    }

    /**
     * Get account by name
     * @param name name
     * @return TwitterAccount
     */
    public static TwitterAccount getAccountByName(String name) {
        PanacheQuery<TwitterAccount> accountQuery = TwitterAccount.find("name", name);
        List<TwitterAccount> accounts = accountQuery.list();
        return accounts.get(0);
    }

    /**
     * Update number of downloaded tweets
     * @param name name
     * @param count number of tweets
     */
    public static void updateTweetCount(String name, int count)
    {
        TwitterAccount acc = getAccountByName(name);
        int oldCount = acc.getTwitterCap();
        int newCount = oldCount+count;
        acc.setTwitterCap(newCount);
        acc.update();
    }


}
