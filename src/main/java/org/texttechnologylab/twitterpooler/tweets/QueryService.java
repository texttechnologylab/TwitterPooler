package org.texttechnologylab.twitterpooler.tweets;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
/**
 * QueryService
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * This class store query's during the current runtime.
 **/
@Singleton
public class QueryService {
    /**
     * List of query's used during the current runtime
     */
    public Map<String, Query> querys = new HashMap<>();


    /**
     * Dummy constructor
     */
    QueryService(){
    };


    /**
     * Add Query to query list.
     * @param query
     */
    public void putQuery(String name, Query query)
    {
        querys.put(name, query);
        System.out.println(name + "query was added to list.");
    }

    /**
     * Get the query from the query list.
     * @return query
     */
    public Query getQuery(String name)
    {
        return querys.get(name);
    }

}
