package edu.harvard.libcomm.pipeline.marc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import org.xml.sax.InputSource;

import org.apache.commons.io.IOUtils;

import org.junit.jupiter.api.Test;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import edu.harvard.libcomm.message.*;

class ModsProcessorTests {

    @Test
    void processMessage() throws Exception {
        ModsProcessor mp = new ModsProcessor();

        LibCommMessage lcm = new LibCommMessage();
        LibCommMessage.Payload pl = new LibCommMessage.Payload();


        InputStream is = new FileInputStream(this.getClass().getResource("/marcxml_sample_1.xml").getFile());


        String xml = IOUtils.toString(is);

        pl.setFormat("marc");
        pl.setData(xml);
        lcm.setPayload(pl);

        mp.processMessage(lcm);

        String result = lcm.getPayload().getData();
        InputStream modsIS = IOUtils.toInputStream(result, "UTF-8");

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setValidating(false);
        builderFactory.setNamespaceAware(false);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document mods = builder.parse(modsIS);
        XPath xPath = XPathFactory.newInstance().newXPath();

        String typeOfResource = (String) xPath.compile("/*//*[local-name() = 'typeOfResource']").evaluate(mods, XPathConstants.STRING);

        assertEquals("text", typeOfResource, "transforms typeOfResource");

        String originCode = (String) xPath.compile("/*//*[local-name()='place'][1]/*[local-name() = 'placeTerm'][@type='code'][@authority='marccountry']").evaluate(mods, XPathConstants.STRING);

        assertEquals("nyu", originCode, "transforms controlfield 008 origin code");

        String originText = (String) xPath.compile("/*//*[local-name()='place'][1]/*[local-name() = 'placeTerm'][@type='text'][@authority='marccountry']").evaluate(mods, XPathConstants.STRING);

        assertEquals("New York (State)", originText, "transforms controlfield 008 origin as text");
    }
}
