package edu.harvard.libcomm.pipeline.marc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import org.xml.sax.InputSource;

import org.apache.commons.io.IOUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;


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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ModsProcessorTests {

    private Document mods;
    private XPath xPath;


    @BeforeAll
    void buildMods() throws Exception {
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
        mods = builder.parse(modsIS);
        xPath = XPathFactory.newInstance().newXPath();
    }


    @Test //LTSCLOUD-393
    void buildModsPlaceTerm() throws Exception {

        String originCode = (String) xPath.compile("/*//*[local-name()='place'][1]/*[local-name() = 'placeTerm'][@type='code'][@authority='marccountry']").evaluate(mods, XPathConstants.STRING);

        assertEquals("nyu", originCode, "transforms controlfield 008 origin code");

        String originText = (String) xPath.compile("/*//*[local-name()='place'][1]/*[local-name() = 'placeTerm'][@type='text'][@authority='marccountry']").evaluate(mods, XPathConstants.STRING);

        assertEquals("New York (State)", originText, "transforms controlfield 008 origin as text");
    }


    @Test //LTSCLOUD-390
    void buildModsLanguage() throws Exception {

        String languageCode = (String) xPath.compile("/*//*[local-name()='language'][1]/*[local-name() = 'languageTerm'][@type='code'][@authority='iso639-2b']").evaluate(mods, XPathConstants.STRING);

        assertEquals("eng", languageCode);

        String languageText = (String) xPath.compile("/*//*[local-name()='language'][1]/*[local-name() = 'languageTerm'][@type='text'][@authority='iso639-2b']").evaluate(mods, XPathConstants.STRING);

        assertEquals("English", languageText);

    }
}
