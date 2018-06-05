package edu.harvard.libcomm.test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.net.URLConnection;
import org.apache.commons.io.IOUtils;
import edu.harvard.libcomm.test.HttpUrlStreamHandler;
import static org.mockito.Mockito.*;


public class TestHelpers {
    private static InputStream is;
    private static boolean setupDone = false;
    private static HttpUrlStreamHandler httpUrlStreamHandler;

    public static String readFile(String resourceName) throws IOException {
        is = new FileInputStream(TestHelpers.class.getResource("/"+resourceName).getFile());
        return IOUtils.toString(is);

        // byte[] encoded = Files.readAllBytes(Paths.get(path));
        // return new String(encoded, StandardCharsets.UTF_8);
    }

    public static HttpUrlStreamHandler getHttpUrlStreamHandler() {
        if(!setupDone) {
            // Allows for mocking URL connections
            URLStreamHandlerFactory urlStreamHandlerFactory = mock(URLStreamHandlerFactory.class);
            URL.setURLStreamHandlerFactory(urlStreamHandlerFactory);
            httpUrlStreamHandler = new HttpUrlStreamHandler();
            when(urlStreamHandlerFactory.createURLStreamHandler("http")).thenReturn(httpUrlStreamHandler);
            setupDone = true;
        }
        return httpUrlStreamHandler;
    }
}
