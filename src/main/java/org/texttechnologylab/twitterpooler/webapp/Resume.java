package org.texttechnologylab.twitterpooler.webapp;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.quarkiverse.freemarker.TemplatePath;
import io.quarkus.mongodb.panache.PanacheQuery;
import org.texttechnologylab.twitterpooler.twitter.QueryAccount;
import org.texttechnologylab.twitterpooler.twitter.TwitterAccount;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.MediaType.TEXT_HTML;
/**
 * Resume
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * This class provides resume endpoint.
 **/
@Path("resume")
public class Resume {

    @Inject
    @TemplatePath("resume.ftl")
    Template home;

    /**
     * Main function for resume endpoint
     * @param uriInfo
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    @GET
    @Produces(TEXT_HTML)
    public String start(@Context UriInfo uriInfo) throws TemplateException, IOException {
        Map<String, Object> params = new HashMap<>();

        //get querys
        PanacheQuery<QueryAccount> queryAcc = QueryAccount.find("status ='Stopped'");
        List<QueryAccount> queryAccounts = queryAcc.list();
        params.put("queryAccounts",queryAccounts);
        //get accounts
        PanacheQuery<TwitterAccount> accountQuery = TwitterAccount.findAll();
        List<TwitterAccount> accounts = accountQuery.list();
        params.put("accounts", accounts);


        StringWriter stringWriter = new StringWriter();
        home.process(params,stringWriter);
        return stringWriter.toString();

    }
}
