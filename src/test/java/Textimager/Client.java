package Textimager;

import io.quarkus.test.junit.QuarkusTest;
import org.hucompute.textimager.client.TextImagerClient;
import org.junit.jupiter.api.Test;
import org.texttechnologylab.utilities.helper.FileUtils;
import org.texttechnologylab.utilities.helper.TempFileHandler;

import java.io.File;
import java.io.IOException;

@QuarkusTest
public class Client {
    @Test
    public void clientTest() throws IOException {
        File tFile = TempFileHandler.getTempFile("aaa", "bbb");
        FileUtils.downloadFile(tFile, "http://service.hucompute.org/urls_v2.xml");

        TextImagerClient tic = new TextImagerClient();
        tic.setConfigFile(tFile.getAbsolutePath());

        String stop;
    }
}
