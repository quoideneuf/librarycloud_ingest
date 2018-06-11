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
import edu.harvard.libcomm.test.TestMessageUtils;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PublishProcessorTests {

    @Test
    void expandRepositoryCodes() throws Exception {
        PublishProcessor p = new PublishProcessor();

        String cloudbody = TestHelpers.readFile("001490591.enrich-end.cloudbody.xml");

        LibCommMessage lcm = TestMessageUtils.unmarshalLibCommMessage(IOUtils.toInputStream(cloudbody, "UTF-8"));

        String xml = lcm.getPayload().getData();

        p.processMessage(lcm);

        String result = lcm.getPayload().getData();

        // byte[] xmlBytes = xml.getBytes();
        // Path p1 = Paths.get("./tmp/publishprocessor_input.xml");
        // Files.write(p1, xmlBytes);

        // byte[] resultBytes = result.getBytes();
        // Path p2 = Paths.get("./tmp/publishprocessor_output.xml");
        // Files.write(p2, resultBytes);

        InputStream modsIS = IOUtils.toInputStream(result, "UTF-8");

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setValidating(false);
        builderFactory.setNamespaceAware(false);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document mods = builder.parse(modsIS);
        XPath xPath = XPathFactory.newInstance().newXPath();

        String repositoryTextChanged = (String) xPath.compile("//*[local-name()='location'][1]/*[local-name()='physicalLocation'][@type = 'repository']").evaluate(mods, XPathConstants.STRING);
        String displayLabelAdded = (String) xPath.compile("//*[local-name()='location'][1]/*[local-name()='physicalLocation'][@type = 'repository']/@displayLabel").evaluate(mods, XPathConstants.STRING);

        assertEquals("Music", repositoryTextChanged);
        assertEquals("Harvard repository", displayLabelAdded);

        String repositoryTextUnchanged = (String) xPath.compile("//*[local-name()='location'][2]/*[local-name()='physicalLocation'][@type = 'repository']").evaluate(mods, XPathConstants.STRING);
        assertEquals("xxx", repositoryTextUnchanged);
    }
}
