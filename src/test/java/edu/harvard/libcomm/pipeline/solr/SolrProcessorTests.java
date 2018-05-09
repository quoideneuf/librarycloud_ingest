package edu.harvard.libcomm.pipeline.solr;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.*;
import org.xml.sax.InputSource;

import org.apache.commons.io.IOUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;



import io.specto.hoverfly.junit.rule.HoverflyRule;

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
import edu.harvard.libcomm.pipeline.MessageUtils;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SolrProcessorTests {

    private Document solrDoc;
    private XPath xPath;

    @BeforeAll
    void buildSolrDoc() throws Exception {
        // SolrProcessor sp = new SolrProcessor();

        LibCommMessage lcm = new LibCommMessage();
        LibCommMessage.Payload pl = new LibCommMessage.Payload();

        InputStream is = new FileInputStream(this.getClass().getResource("/modsxml_sample_1.xml").getFile());
        String xml = IOUtils.toString(is);

        pl.setFormat("mods");
        pl.setData(xml);
        lcm.setPayload(pl);

        String result = MessageUtils.transformPayloadData(lcm, "src/main/resources/mods2solr.xsl", null);

        InputStream solrIS = IOUtils.toInputStream(result, "UTF-8");

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setValidating(false);
        builderFactory.setNamespaceAware(false);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        solrDoc = builder.parse(solrIS);
        xPath = XPathFactory.newInstance().newXPath();
    }


    @Test
    void buildSolrLanguageFields() throws Exception {

        String languageCode = (String) xPath.compile("//field[@name='languageCode']").evaluate(solrDoc, XPathConstants.STRING);
        assertEquals("eng", languageCode);

        String language = (String) xPath.compile("//field[@name='language']").evaluate(solrDoc, XPathConstants.STRING);
        assertEquals("English", language);
    }
}
