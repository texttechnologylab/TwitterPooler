package webapp;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.quarkiverse.freemarker.TemplatePath;
import io.quarkus.mongodb.panache.PanacheQuery;
import org.apache.http.HttpEntity;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;
import tweets.Endpoint;
import tweets.Query;
import tweets.QueryService;
import tweets.RequestHandler;
import twitter.TwitterAccount;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import static javax.ws.rs.core.MediaType.TEXT_HTML;
/**
 * Count
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * This class provides count endpoint.
 **/
@Path("count")
public class Count {
    @Inject
    @TemplatePath("count.ftl")
    Template home;

    @Inject
    QueryService queryService;

    /**
     * Main function for count endpoint
     * @param uriInfo
     * @return
     * @throws IOException
     * @throws TemplateException
     * @throws URISyntaxException
     */
    @GET
    @Produces(TEXT_HTML)
    public String count(@Context UriInfo uriInfo) throws IOException, TemplateException, URISyntaxException {
        Map<String, Object> params = new HashMap<>();

        boolean search = uriInfo.getQueryParameters().containsKey("search");
        //initialize Query class
        Query query = new Query();

        //create STools string
        if(uriInfo.getQueryParameters().get("STools") != null)
        {
            query.setSTools(parseSTools(uriInfo));
        }
        //Create uniqueID
        String uniqueID = UUID.randomUUID().toString();
        query.setUniqueId(uniqueID);

        //set max result if not empty
        if(uriInfo.getQueryParameters().get("maxResult") != null && !uriInfo.getQueryParameters().get("maxResult").get(0).equals(""))
        {
            query.setMaxResults(uriInfo.getQueryParameters().get("maxResult").get(0));
        }

        //get accounts
        PanacheQuery<TwitterAccount> accountQuery = TwitterAccount.findAll();
        List<TwitterAccount> accounts = accountQuery.list();
        params.put("accounts", accounts);

        //String startTime = "2021-11-10T00:00:00Z";
        String startTime = null;
        if(uriInfo.getQueryParameters().get("startTime") != null && !uriInfo.getQueryParameters().get("startTime").get(0).equals(""))
        {startTime = query.dateStringFormatting(uriInfo.getQueryParameters().get("startTime").get(0));}
        String endTime = null;
        if(uriInfo.getQueryParameters().get("endTime") != null && !uriInfo.getQueryParameters().get("endTime").get(0).equals(""))
        {endTime = query.dateStringFormatting(uriInfo.getQueryParameters().get("endTime").get(0));}

        if(search)
        {
            //create count query and show count site
            String searchQuery = uriInfo.getQueryParameters().get("search").get(0);

            //add params to query
            query.setQuery(searchQuery);
            query.setStartTime(startTime);
            query.setEndTime(endTime);

            String endpointValue = "count";

                endpointValue = endpointValue + uriInfo.getQueryParameters().get("endpHash").get(0);
                query.setEndpointValue("search"+uriInfo.getQueryParameters().get("endpHash").get(0));
            //get default TwitterAccount
            TwitterAccount account = accounts.get(0);

            //get endpoint
            Endpoint endpoint = new Endpoint();
            endpoint.setEndp(Endpoint.getEndpoint(endpointValue, endpoint.endpointList));

            URIBuilder uri;
            //create uri
            uri = query.createCountQuery(query.getQuery(),query.getStartTime(),query.getEndTime(),endpoint.getEndp());

            RequestHandler requestHandler = new RequestHandler();
            HttpEntity entity = requestHandler.sendRequest(uri, account.getBearerToken(), requestHandler.client);
            JSONObject result = requestHandler.parseToJson(entity);

            query.setTweetCount((int)((JSONObject)result.get("meta")).get("total_tweet_count"));

            params.put("tweetCount", query.getTweetCount());
            params.put("queryId", query.getUniqueId());
            queryService.putQuery(query.getUniqueId(), query);
        }
        else
        {
            //start endtime
            query.setStartTime(startTime);
            query.setEndTime(endTime);

            for (var e : uriInfo.getQueryParameters().entrySet()) {
                // include only the first instance of each query parameter
                if(!e.getKey().equals("startTime") && !e.getKey().equals("subBtn") && !e.getKey().equals("maxResult") &&
                        !e.getKey().equals("endTime") && !e.getKey().equals("endpHash") && !e.getKey().equals("endpKeywords"))
                {
                    query.setQuery(e.getValue().get(0));
                    query.setEndpointValue(e.getKey());
                }
            }

            params.put("queryId", query.getUniqueId());
            queryService.putQuery(query.getUniqueId(), query);

        }

        //find all Twitter accounts
        StringWriter stringWriter = new StringWriter();
        home.process(params,stringWriter);
        return stringWriter.toString();
    }

    /**
     * Parse selected NLP Tools into one string
     * @param uriInfo
     * @return String
     */
    private String parseSTools(UriInfo uriInfo)
        {
            StringBuilder STools = new StringBuilder();
            var strings = uriInfo.getQueryParameters().get("STools");
            for(int i = 0;i<strings.size();i++)
            {
                if (i==0)
                {
                    STools.append(strings.get(i));
                }
                else
                {
                    STools.append(","+ strings.get(i));
                }
            }

            return STools.toString();
        }
}
