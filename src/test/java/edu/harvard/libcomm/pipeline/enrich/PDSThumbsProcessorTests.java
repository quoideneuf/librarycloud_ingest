package edu.harvard.libcomm.pipeline.enrich;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.nio.file.Files;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;

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
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Disabled;

import edu.harvard.libcomm.pipeline.MessageUtils;
import edu.harvard.libcomm.test.TestHelpers;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PDSThumbsProcessorTests {

    @Test
    void dontObliterateExistingThumbnails() throws Exception {
        PDSThumbsProcessor p = new PDSThumbsProcessor();

        LibCommMessage lcm = new LibCommMessage();
        LibCommMessage.Payload pl = new LibCommMessage.Payload();

        String xml = TestHelpers.readFile("001763319.PDSThumbsProcessor.xml");
        pl.setFormat("mods");
        pl.setData(xml);
        lcm.setPayload(pl);

        // String urns = MessageUtils.transformPayloadData(lcm,"src/main/resources/pds_urns.xsl",null);

        // assertEquals("http://nrs.harvard.edu/urn-3:fhcl.loeb:10356603,http://nrs.harvard.edu/urn-3:fhcl.loeb:9961581,", urns);

        p.processMessage(lcm);

        String result = lcm.getPayload().getData();

        byte[] xmlBytes = xml.getBytes();
        Path p1 = Paths.get("./tmp/pdsthumbs_input.xml");
        Files.write(p1, xmlBytes);

        byte[] resultBytes = result.getBytes();
        Path p2 = Paths.get("./tmp/pdsthumbs_output.xml");
        Files.write(p2, resultBytes);

        InputStream modsIS = IOUtils.toInputStream(result, "UTF-8");

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setValidating(false);
        builderFactory.setNamespaceAware(false);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document mods = builder.parse(modsIS);
        XPath xPath = XPathFactory.newInstance().newXPath();

        String thumb1Url = (String) xPath.compile("//*[local-name()='mods'][2]//*[local-name()='url'][@access='preview']").evaluate(mods, XPathConstants.STRING);

        assertEquals("http://ids.lib.harvard.edu/ids/view/45562415?width=150&height=150&usethumb=y", thumb1Url);


    }
}
