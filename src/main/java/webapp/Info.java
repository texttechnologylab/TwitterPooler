package webapp;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.quarkiverse.freemarker.TemplatePath;
import io.quarkus.mongodb.MongoClientName;
import io.quarkus.mongodb.panache.PanacheQuery;
import org.apache.http.NameValuePair;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.methods.HttpGet;
import org.bson.Document;
import org.eclipse.jetty.server.Request;
import org.hucompute.textimager.client.TextImagerClient;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.springframework.util.MultiValueMap;
import textimager.ClientTextimager;
import tweets.PoolingService;
import tweets.Query;
import tweets.QueryService;
import tweets.RequestService;
import twitter.PoolAccount;
import twitter.QueryAccount;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.net.http.HttpClient;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.MediaType.TEXT_HTML;
/**
 * Info
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * This class provides info and start endpoint.
 **/
@Path("info")
public class Info {

    @Inject
    @TemplatePath("info.ftl")
    Template home;

    @Inject
    QueryService queryService;

    @Inject
    @MongoClientName("tweets")
    MongoClient mongoClientTweets;

    @Inject
    @MongoClientName("pools")
    MongoClient mongoClientPools;


    public MultivaluedMap<String, String> tempUriInfo = new MultivaluedMapImpl<>();

    /**
     * This is main function which check if requested pool/query already exists and take appropriate action
     * @param uriInfo
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    @GET
    @Produces(TEXT_HTML)
    public String info(@Context UriInfo uriInfo) throws TemplateException, IOException {
        //set global uri info

        Map<String, Object> params = new HashMap<>();
        tempUriInfo = uriInfo.getQueryParameters();

        MongoDatabase mongoClient = mongoClientTweets.getDatabase("Tweets");
        //create request
        if(!uriInfo.getQueryParameters().containsKey("btn")){


            boolean notExist = true;
            Query query = getQuery();
            //check if querys exits
            QueryAccount acc = null;
            String status = "";
            try{
                acc = QueryAccount.getQueryAccount(query.getUniqueId());
                status = acc.getStatus();
            }
            catch (Exception e){}

            List<QueryAccount> accCheckList = QueryAccount.getQueryAccountsByQuery(query.getQuery());
            if (accCheckList.size()>0 && !status.equals("Stopped"))
            {
                Map<String, LocalDate> dates = getQueryAccountsDates(accCheckList);
                LocalDate queryAccountFirstDate=dates.get("first");
                LocalDate queryAccountLastDate=dates.get("last");

                Map<String, LocalDate> queryDates = getQueryDates(query);
                LocalDate queryFirstDate=queryDates.get("first");//najstarsze
                LocalDate queryLastDate=queryDates.get("last");//najmlodsze

                //tutaj zacznij sprawdzanie
                if(queryFirstDate == null)//ten przypadek jest mozliwy tylko w fullarchive
                {
                    params.put("check", "This query was already made in the past. There are entries in the database from "+queryAccountFirstDate.toString()+" to "+ queryAccountLastDate.toString()+". If you want to continue anyway, click Start anyway");
                    params.put("continueBtn", "");
                    notExist = false;
                }
                else
                {
                    if((queryAccountFirstDate.compareTo(queryFirstDate)<=0) && (queryAccountLastDate.compareTo(queryLastDate)>=0))
                    {
                        params.put("check", "This query was already made in the past.");
                        notExist = false;
                    }
                    else if(!queryFirstDate.isAfter(queryAccountLastDate))
                    {
                        params.put("check", "This query was already made in the past. There are entries in the database from "+queryAccountFirstDate.toString()+" to "+ queryAccountLastDate.toString()+". If you want to continue anyway, click Start anyway");
                        params.put("continueBtn", "");
                        notExist = false;
                    }
                }

            }
            if(notExist)
            {
                ClientTextimager text = new ClientTextimager();
                TextImagerClient client = text.createClient();

                RequestService requestService = new RequestService();
                requestService.start(uriInfo.getQueryParameters().get("twitterAccount").get(0),query, mongoClient, client, query.getSTools());
            }
        }
        else
        {
            MongoDatabase mongoPools = mongoClientPools.getDatabase("Pools");
            MongoCollection<Document> mongoCollection = mongoClient.getCollection(uriInfo.getQueryParameters().get("queryId").get(0));
            String poolingArt = uriInfo.getQueryParameters().get("btn").get(0);
            String topics = uriInfo.getQueryParameters().get("terms").get(0);
            //do pooling staff
            Map<String,Integer> poolValues = selectPoolValues(poolingArt, uriInfo);

            //Check if pools exits
            String prefix = createPrefix(poolingArt,poolValues);
            PanacheQuery<PoolAccount> check = PoolAccount.find("queryId='"+uriInfo.getQueryParameters().get("queryId").get(0)+"' and poolArt='"+prefix+"'");
            List<PoolAccount> accounts = check.list();

            if(accounts.size()==0)
            {
                PoolingService poolingService = new PoolingService();
                poolingService.start(mongoPools, mongoCollection, poolingArt,uriInfo.getQueryParameters().get("queryId").get(0), topics , poolValues);
            }
            else
            {
                params.put("check", "This pool already exists");
            }
        }

        //show message and redirect to home page after click the button
        StringWriter stringWriter = new StringWriter();
        home.process(params,stringWriter);
        return stringWriter.toString();
    }

    /**
     * Start the request
     * @throws IOException
     */
    @GET
    @Path("/start")
    public void startAction() throws IOException {
        try {
            MongoDatabase mongoClient = mongoClientTweets.getDatabase("Tweets");
            Query query = getQuery();

            ClientTextimager text = new ClientTextimager();
            TextImagerClient client = text.createClient();

            RequestService requestService = new RequestService();
            requestService.start(tempUriInfo.get("twitterAccount").get(0), query, mongoClient, client, query.getSTools());
            tempUriInfo = null;
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    /**
     * Parse query dates
     * @param query
     * @return
     */
    private Map<String, LocalDate> getQueryDates(Query query)
    {
        Map<String, LocalDate> list = new HashMap<>();

        LocalDate queryFirstDate=null;//najstarsze
        LocalDate queryLastDate=null;//najmlodsze
        if(query.getStartTime()!=null)
        {
            queryFirstDate = LocalDate.parse(query.getStartTime(),DateTimeFormatter.ISO_DATE_TIME);
        }
        if(query.getEndTime()!=null)
        {
            queryLastDate = LocalDate.parse(query.getEndTime(),DateTimeFormatter.ISO_DATE_TIME);
        }
        //jesli start i end datum to najprosciej
        if (queryFirstDate == null)
        {
            //tutaj sprawdzamy endpoint i liczymy date
            if (query.getEndpointValue().contains("Recent"))
            {
                Instant instant = Instant.now();
                LocalDate localDate = LocalDate.ofInstant(instant, ZoneOffset.UTC);
                queryFirstDate = localDate.minusDays(7);
            }
        }

        if(queryLastDate == null)
        {
            Instant instant = Instant.now();
            LocalDate localDate = LocalDate.ofInstant(instant, ZoneOffset.UTC);
            queryLastDate = localDate;
        }

        list.put("first", queryFirstDate);
        list.put("last",queryLastDate);
        return list;
    }

    /**
     * Parse dates from existing query's
     * @param accCheckList
     * @return
     */
    private Map<String, LocalDate> getQueryAccountsDates(List<QueryAccount> accCheckList)
    {
        Map<String, LocalDate> list = new HashMap<>();
        LocalDate queryAccountFirstDate=null;
        LocalDate queryAccountLastDate=null;

        for (int i = 0; i<accCheckList.size();i++)
        {
            var dates = accCheckList.get(i).getDateRange().split("/");
            int count=0;
            for(String date:dates)
            {
                if(count==0)
                {
                    LocalDate first = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
                    count+=1;
                    if(queryAccountFirstDate==null)
                    {
                        queryAccountFirstDate = first;
                    }
                    else if(first.isBefore(queryAccountFirstDate))
                    {
                        queryAccountFirstDate = first;
                    }
                }
                else
                {
                    LocalDate last = LocalDate.parse(date,DateTimeFormatter.ISO_DATE);

                    if(queryAccountLastDate==null)
                    {
                        queryAccountLastDate = last;
                    }
                    else if(last.isAfter(queryAccountLastDate))
                    {
                        queryAccountLastDate = last;
                    }
                }
            }
        }

        list.put("first", queryAccountFirstDate);
        list.put("last", queryAccountLastDate);
        return list;
    }

    /**
     * Get query from QueryService or Database
     * @return
     */
    private Query getQuery()
    {
        Query query = queryService.getQuery(tempUriInfo.get("queryId").get(0));
        if (query == null)
        {
            QueryAccount qe = QueryAccount.getQueryAccount(tempUriInfo.get("queryId").get(0));
            query = initQuery(qe,tempUriInfo.get("maxResult").get(0));
        }
        int updatedTweetCount = updateTweetCount(tempUriInfo.get("queryId").get(0));

        if(updatedTweetCount != 0)
        {
            query.setTweetCount(updatedTweetCount);
        }
        if(query.getMaxResults()!=null){
        if(query.getMaxResults().equals("0"))
        {
            try{query.setMaxResults(tempUriInfo.get("maxResult").get(0));}catch (Exception e){}
        }}
        return query;
    }

    /**
     * Get updated number of downloaded tweets
     * @param queryId
     * @return int
     */
    private int updateTweetCount(String queryId)
    {
        try
        {
            QueryAccount qe = QueryAccount.getQueryAccount(queryId);
            return qe.getAllTweets() - qe.getTweetCount();
        }
        catch(Exception e)
        {
            return 0;
        }
    }

    /**
     * Initialize Query class from QueryAccount
     * @param qr
     * @param maxResult
     * @return
     */
    private Query initQuery(QueryAccount qr, String maxResult)
    {
        Query query = new Query();
        query.setUniqueId(qr.getQueryId());
        query.setUserName(qr.getUser());
        query.setNextToken(qr.getNextToken());
        query.setQuery(qr.getQuery());
        query.setEndpointValue(qr.getEndpoint());
        query.setTweetCount(qr.getAllTweets()- qr.getTweetCount());
        if (!maxResult.equals(""))
        {
            query.setMaxResults(maxResult);
        }

        return query;
    }

    /**
     * Parse selected pool values
     * @param poolingArt
     * @param uriInfo
     * @return
     */
    private Map<String, Integer> selectPoolValues(String poolingArt, UriInfo uriInfo)
    {
        Map<String, Integer> result = new HashMap<>();

        if(poolingArt.equals("temporalDays"))
        {
            int size = Integer.parseInt(uriInfo.getQueryParameters().get("selectSizeTempDays").get(0));
            int limit = Integer.parseInt(uriInfo.getQueryParameters().get("selectLimitTempDays").get(0));
            result.put("size", size);
            result.put("limit", limit);
            return result;
        }
        else if(poolingArt.equals("hashtag"))
        {
            int limit = Integer.parseInt(uriInfo.getQueryParameters().get("selectLimitHashtag").get(0));
            result.put("limit", limit);
            return result;
        }
        else if (poolingArt.equals("author"))
        {
            int limit = Integer.parseInt(uriInfo.getQueryParameters().get("selectLimitAuthor").get(0));
            result.put("limit", limit);
            return result;
        }
        else if (poolingArt.equals("temporalHours"))
        {
            int size = Integer.parseInt(uriInfo.getQueryParameters().get("selectSizeTempHours").get(0));
            int limit = Integer.parseInt(uriInfo.getQueryParameters().get("selectLimitTempHours").get(0));
            result.put("size", size);
            result.put("limit", limit);
            return result;
        }
        else if (poolingArt.equals("burst"))
        {
            int size = Integer.parseInt(uriInfo.getQueryParameters().get("selectSizeDaysBrust").get(0));
            int limit = Integer.parseInt(uriInfo.getQueryParameters().get("selectLimitBurst").get(0));
            result.put("limit", limit);
            result.put("size", size);
            return result;
        }

        return null;
    }

    /**
     * Create prefix for PoolAccounts
     * @param poolingArt
     * @param poolValues
     * @return
     */
    private String createPrefix(String poolingArt, Map<String,Integer> poolValues)
    {
        String prefix="";
        Integer limit = poolValues.get("limit");

        if(poolingArt.equals("author"))
        {
            prefix = "A"+"L"+Integer.toString(limit);
        }
        else if (poolingArt.equals("hashtag"))
        {
            prefix = "H"+"L"+Integer.toString(limit);
        }
        else if(poolingArt.equals("temporalDays"))
        {
            prefix="TDays"+"S"+Integer.toString(poolValues.get("size"))+"L"+Integer.toString(limit);

        }
        else if(poolingArt.equals("temporalHours"))
        {
            prefix="THours"+"S"+Integer.toString(poolValues.get("size"))+"L"+Integer.toString(limit);
        }
        else if (poolingArt.equals("burst"))
        {
            prefix="B"+"L"+Integer.toString(limit);
        }
        return prefix;
    }
}
