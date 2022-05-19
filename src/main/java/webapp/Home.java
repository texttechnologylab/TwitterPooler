package webapp;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.quarkiverse.freemarker.TemplatePath;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.runtime.annotations.QuarkusMain;
import org.springframework.core.io.ClassPathResource;
import twitter.PoolAccount;
import twitter.QueryAccount;
import twitter.TwitterAccount;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javax.ws.rs.core.MediaType.TEXT_HTML;
/**
 * Home
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * This class provides home endpoint.
 **/
@Path("home")
public class Home {
    @Inject
    @TemplatePath("home.ftl")
    Template home;

    /**
     * Main function for home endpoint
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    @GET
    @Produces(TEXT_HTML)
    public String home() throws IOException, TemplateException {

        //


        PanacheQuery<TwitterAccount> accountQuery = TwitterAccount.findAll();
        List<TwitterAccount> accounts = accountQuery.list();

        checkTweetCapResetDay(accounts);


        //StringBuilder imagePath = new StringBuilder();
        //imagePath.append(request.getContextPath()).append(File.separator).append("images").append(File.separator) ;
        //InputStream content = context.getResourceAsStream("/images/logo.jpg");

        //PanacheQuery<QueryAccount> queryAcc = QueryAccount.findAll();
        PanacheQuery<QueryAccount> queryAcc = QueryAccount.find("status !='Complete'");
        List<QueryAccount> queryAccounts = queryAcc.list();

        PanacheQuery<PoolAccount> poolAcc = PoolAccount.find("status !='Complete'");
        List<PoolAccount> poolAccounts = poolAcc.list();

        //File file = new ClassPathResource("freemarker/templates/images/Logo.png").getFile();

        Map<String, Object> params = new HashMap<>();
        //params.put("logo", file);
        params.put("twitterAccounts", accounts);
        params.put("name", "Test");
        params.put("queryAccounts",queryAccounts);
        params.put("poolAccounts",poolAccounts);

        StringWriter stringWriter = new StringWriter();
        home.process(params,stringWriter);
        return stringWriter.toString();
    }

    /**
     * Check if tweet cap can be reset
     * @param accounts
     */
    private void checkTweetCapResetDay(List<TwitterAccount> accounts)
    {
        for (int i =0 ; i<accounts.size();i++)
        {

            //LocalDate oldDate = LocalDate.ofInstant(accounts.get(i).getReset().toInstant(), ZoneId.systemDefault());
            LocalDateTime oldDate = accounts.get(i).getReset();
            var newDate = Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String text = oldDate.format(formatter);
            LocalDateTime parsedDate = LocalDateTime.parse(text, formatter);

            if(newDate.isAfter(parsedDate))
            {
                //set new Date
                LocalDateTime date = parsedDate.plusMonths(1).withSecond(1);

                //reset Cap
                accounts.get(i).setTwitterCap(0);
                accounts.get(i).setReset(date);
                accounts.get(i).update();
            }
        }
    }
}
