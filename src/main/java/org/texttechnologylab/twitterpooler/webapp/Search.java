package org.texttechnologylab.twitterpooler.webapp;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.quarkiverse.freemarker.TemplatePath;
import org.apache.xerces.dom.DeferredElementImpl;
import org.texttechnologylab.utilities.helper.FileUtils;
import org.texttechnologylab.utilities.helper.TempFileHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

import static javax.ws.rs.core.MediaType.TEXT_HTML;
/**
 * Search
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * This class provides search endpoint.
 **/
@Path("search")
public class Search {
    @Inject
    @TemplatePath("search.ftl")
    Template home;

    /**
     * Main function for search endpoint
     * @return
     * @throws IOException
     * @throws TemplateException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    @GET
    @Produces(TEXT_HTML)
    public String search() throws IOException, TemplateException, ParserConfigurationException, SAXException {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "Test");

        List<String> sTools = createSToolList();
        params.put("nlpTools", sTools);

        StringWriter stringWriter = new StringWriter();
        home.process(params,stringWriter);
        return stringWriter.toString();
    }

    /**
     * Create list with available nlp tools
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private List<String> createSToolList () throws ParserConfigurationException, IOException, SAXException {
        List<String> sTools = new ArrayList<>();

        File file = TempFileHandler.getTempFile("aaa", "bbb");
        FileUtils.downloadFile(file, "http://service.hucompute.org/urls_v2.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = null;
        builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        NodeList nodes = doc.getElementsByTagName("webservice");
        for (int i=0; i<nodes.getLength();i++)
        {
            Node node = nodes.item(i);
            String name = ((DeferredElementImpl) node).getAttribute("name");
            String visible = ((DeferredElementImpl) node).getAttribute("visible");
            if(!visible.equals("false"))
            {
                sTools.add(name);
            }
        }
        Collections.sort(sTools);
        return sTools;
    }
}
