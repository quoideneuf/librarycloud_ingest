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


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PDSThumbsProcessorTests {

    @Test
    @Disabled
    void testTest() throws Exception {
        String id = "008154415";
        //String id = "001490591";
        PDSThumbsProcessor p = new PDSThumbsProcessor();

        LibCommMessage lcm = new LibCommMessage();
        LibCommMessage.Payload pl = new LibCommMessage.Payload();

        InputStream is = new FileInputStream(this.getClass().getResource("/"+id).getFile());

        String xml = IOUtils.toString(is);
        //        System.out.println(xml);

        pl.setFormat("mods");
        pl.setData(xml);
        lcm.setPayload(pl);

        p.processMessage(lcm);

        String result = lcm.getPayload().getData();
        //        System.out.println(result);

        byte[] xmlBytes = xml.getBytes();
        Path p1 = Paths.get("./tmp/thumbs_input.xml");
        Files.write(p1, xmlBytes);

        byte[] resultBytes = result.getBytes();
        Path p2 = Paths.get("./tmp/thumbs_output.xml");
        Files.write(p2, resultBytes);


        InputStream modsIS = IOUtils.toInputStream(result, "UTF-8");

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setValidating(false);
        builderFactory.setNamespaceAware(false);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document mods = builder.parse(modsIS);
        XPath xPath = XPathFactory.newInstance().newXPath();
    }
}
