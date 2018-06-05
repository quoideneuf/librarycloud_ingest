package edu.harvard.libcomm.pipeline.enrich;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLStreamHandlerFactory;
import java.net.URLConnection;

import java.util.Date;
import java.nio.file.Files;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.*;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;
import edu.harvard.libcomm.pipeline.Config;
import edu.harvard.libcomm.pipeline.IProcessor;
import edu.harvard.libcomm.pipeline.MessageUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Disabled;

import static org.mockito.Mockito.*;

import edu.harvard.libcomm.test.TestHelpers;
import edu.harvard.libcomm.test.HttpUrlStreamHandler;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DRSExtensionsProcessorTests {

    private static HttpUrlStreamHandler httpUrlStreamHandler;

    @BeforeAll
    public static void setupURLStreamHandlerFactory() {
        httpUrlStreamHandler = TestHelpers.getHttpUrlStreamHandler();
    }

    @BeforeEach
    public void reset() {
        httpUrlStreamHandler.resetConnections();
    }

    @Test
    void addDRSExtensionsData() throws Exception {

        DRSExtensionsProcessor p = new DRSExtensionsProcessor();

        LibCommMessage lcm = new LibCommMessage();
        LibCommMessage.Payload pl = new LibCommMessage.Payload();

        String xml = TestHelpers.readFile("enrich-05-modscollection.xml");

        pl.setFormat("mods");
        pl.setData(xml);
        lcm.setPayload(pl);

        String urns = MessageUtils.transformPayloadData(lcm,"src/main/resources/urns.xsl",null).replace(" ", "+");

        String solrResponse = TestHelpers.readFile("solr_extensions_response_001490591.json");

        String href = Config.getInstance().SOLR_EXTENSIONS_URL + "/select?q=urn_keyword:("+urns+")&rows=250";

        URLConnection urlConnection = mock(URLConnection.class);
        httpUrlStreamHandler.addConnection(new URL(href), urlConnection);

        InputStream stream = new ByteArrayInputStream(solrResponse.getBytes(StandardCharsets.UTF_8));
        when(urlConnection.getInputStream()).thenReturn(stream);

        p.processMessage(lcm);

        String result = lcm.getPayload().getData();
        // System.out.println(result);

        // byte[] xmlBytes = xml.getBytes();
        // Path p1 = Paths.get("./tmp/extensions_input.xml");
        // Files.write(p1, xmlBytes);

        // byte[] resultBytes = result.getBytes();
        // Path p2 = Paths.get("./tmp/extensions_output.xml");
        // Files.write(p2, resultBytes);

        InputStream modsIS = IOUtils.toInputStream(result, "UTF-8");

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setValidating(false);
        builderFactory.setNamespaceAware(false);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document mods = builder.parse(modsIS);
        XPath xPath = XPathFactory.newInstance().newXPath();

        String thumb1Url = (String) xPath.compile("//*[local-name()='mods'][1]//*[local-name()='url'][@access='preview']").evaluate(mods, XPathConstants.STRING);

        assertEquals("http://ids.lib.harvard.edu/ids/view/8316207?width=150&height=150&usethumb=y", thumb1Url);

        //Test case with an empty <url access="preview" /> tag
        String thumb2Url = (String) xPath.compile("//*[local-name()='mods'][2]//*[local-name()='url'][@access='preview']").evaluate(mods, XPathConstants.STRING);

        assertEquals("http://ids.lib.harvard.edu/ids/view/8316207?width=150&height=150&usethumb=y", thumb2Url);

        // Test case with an existing non-empty <url access="preview"> tag
        String thumb3Url = (String) xPath.compile("//*[local-name()='mods'][3]//*[local-name()='url'][@access='preview']").evaluate(mods, XPathConstants.STRING);

        assertEquals("http://dontreplaceme.com/55555", thumb3Url);

        String thumb4Url = (String) xPath.compile("//*[local-name()='mods'][4]//*[local-name()='url'][@access='preview']").evaluate(mods, XPathConstants.STRING);

        assertEquals("http://ids.lib.harvard.edu/ids/view/421568540?width=150&height=150&usethumb=y", thumb4Url);



    }
}
