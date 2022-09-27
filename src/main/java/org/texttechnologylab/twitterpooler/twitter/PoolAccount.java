package org.texttechnologylab.twitterpooler.twitter;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.mongodb.panache.common.MongoEntity;

import java.util.List;
/**
 * PoolAccount
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * This class provides pool account and all its attributes.
 **/
@MongoEntity(clientName="admin",database = "administration" ,collection = "Pools")
public class PoolAccount extends PanacheMongoEntity {

    String queryId="";
    String poolArt = "";
    int numberOfPools=0;
    int numberOfCollections=0;
    String createdAt;
    String status="";
    String one="";

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getMulti() {
        return multi;
    }

    public void setMulti(String multi) {
        this.multi = multi;
    }

    String multi="";

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getPoolArt() {
        return poolArt;
    }

    public void setPoolArt(String poolArt) {
        this.poolArt = poolArt;
    }

    public int getNumberOfPools() {
        return numberOfPools;
    }

    public void setNumberOfPools(int numberOfPools) {
        this.numberOfPools = numberOfPools;
    }

    public int getNumberOfCollections() {
        return numberOfCollections;
    }

    public void setNumberOfCollections(int numberOfCollections) {
        this.numberOfCollections = numberOfCollections;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get pool account
     * @param queryId queryId
     * @param poolingArt pooling art
     * @return PoolAccount
     */
    public static PoolAccount getPoolAccount(String queryId, String poolingArt)
    {
        PanacheQuery<PoolAccount> check = PoolAccount.find("queryId = ?1 and poolArt = ?2", queryId, poolingArt);
        List<PoolAccount> accounts = check.list();
        return accounts.get(0);
    }
}
