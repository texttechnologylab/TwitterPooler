package org.texttechnologylab.twitterpooler.textimager;

import org.hucompute.textimager.client.TextImagerClient;
import org.texttechnologylab.utilities.helper.FileUtils;
import org.texttechnologylab.utilities.helper.TempFileHandler;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;

/**
 * ClientTextimager
 *
 * @date 03.01.2022
 *
 * @author Grzegorz Siwiecki
 * @version 1.0
 *
 * This class provides TextimagerClient.
 **/
@ApplicationScoped
public class ClientTextimager {

    String STools;

    public String getSTools() {
        return STools;
    }

    public void setSTools(String STools) {
        this.STools = STools;
    }

    public ClientTextimager()
    {

    }
    /**
     * This function create a client for Textimager.
     * @return TextImagerClient
     */
    public TextImagerClient createClient() throws IOException {
        File tFile = TempFileHandler.getTempFile("aaa", "bbb");
        FileUtils.downloadFile(tFile, "http://service.hucompute.org/urls_v2.xml");

        TextImagerClient tic = new TextImagerClient();
        tic.setConfigFile(tFile.getAbsolutePath());
        return tic;
    }

}
