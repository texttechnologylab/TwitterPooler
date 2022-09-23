package org.texttechnologylab.twitterpooler.tweets;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
/**
 * RequestHandler
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * This class provides http request's and some help functions.
 **/
public class RequestHandler {
    /**
     * Http client
     */
    // from https://github.com/twitterdev/Twitter-API-v2-sample-code
    public HttpClient client = HttpClients.custom()
            .setDefaultRequestConfig(RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD).build())
            .build();

    /**
     * Send request
     * @param uriBuilder created URI
     * @param bearerToken bearer token from Twitter dev account
     * @param client http client
     * @return response, as Http entity
     * @throws URISyntaxException
     * @throws IOException
     */
    // from https://github.com/twitterdev/Twitter-API-v2-sample-code
    public HttpEntity sendRequest(URIBuilder uriBuilder, String bearerToken , HttpClient client ) throws URISyntaxException, IOException {

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpGet.setHeader("Content-Type", "application/json");

        HttpResponse response = client.execute(httpGet);
        return response.getEntity();
    }

    /**
     * Create JSON from HttpEntity
     * @param entity response HttpEntity
     * @return JSON
     * @throws IOException
     */
    public JSONObject parseToJson(HttpEntity entity) throws IOException {
        JSONObject result = new JSONObject(EntityUtils.toString(entity, "UTF-8"));
        return  result;
    }

    /**
     * Create JSONArray from HttpEntity
     * @param entity
     * @return JSONArray
     * @throws IOException
     */
    public JSONArray parseToJsonArray(HttpEntity entity) throws IOException {
        var temp = EntityUtils.toString(entity, "UTF-8");
        JSONObject ds = new JSONObject(temp);
        JSONArray jasonArray = ds.toJSONArray(ds.names());
        return  jasonArray;
    }


}
