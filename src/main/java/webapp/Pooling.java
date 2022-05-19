package webapp;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.quarkiverse.freemarker.TemplatePath;
import io.quarkus.mongodb.panache.PanacheQuery;
import twitter.QueryAccount;
import twitter.TwitterAccount;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.MediaType.TEXT_HTML;
/**
 * Pooling
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * This class provides pooling endpoint.
 **/
@Path("pooling")
public class Pooling {
    @Inject
    @TemplatePath("pooling.ftl")
    Template home;

    /**
     * Main function for polling endpoint
     * @param uriInfo
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    @GET
    @Produces(TEXT_HTML)
    public String start(@Context UriInfo uriInfo) throws TemplateException, IOException {
        Map<String, Object> params = new HashMap<>();

        PanacheQuery<QueryAccount> queryAcc = QueryAccount.find("status ='Complete'");
        List<QueryAccount> queryAccounts = queryAcc.list();
        params.put("queryAccounts",queryAccounts);


        StringWriter stringWriter = new StringWriter();
        home.process(params,stringWriter);
        return stringWriter.toString();

    }
}
