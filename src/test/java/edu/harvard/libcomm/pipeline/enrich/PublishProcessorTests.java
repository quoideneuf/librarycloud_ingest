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
import javax.xml.parsers.ParserConfigurationException;

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
    PublishProcessor p;

    @BeforeAll
    void setup() {
        p = new PublishProcessor();
    }

    @Test
    void expandRepositoryCodes() throws Exception {

        LibCommMessage lcm = TestHelpers.buildLibCommMessage("mods", "publish-processor-tests-sample-1.xml");

        p.processMessage(lcm);
        Document doc = TestHelpers.extractXmlDoc(lcm);

        String repositoryTextChanged = TestHelpers.getXPath("//*[local-name()='location'][1]/*[local-name()='physicalLocation'][@type = 'repository']", doc);
        String displayLabelAdded = TestHelpers.getXPath("//*[local-name()='location'][1]/*[local-name()='physicalLocation'][@type = 'repository']/@displayLabel", doc);
        String extensionValue = TestHelpers.getXPath("//*[local-name()='HarvardRepositories']/*[local-name()='HarvardRepository']/text()", doc);
        String valueURI = TestHelpers.getXPath("//*[local-name()='location'][1]/*[local-name()='physicalLocation'][@type = 'repository']/@valueURI", doc);

        // System.out.println(lcm.getPayload().getData());
        assertEquals("African and African American Studies Reading Room, Harvard University", repositoryTextChanged);
        assertEquals("Harvard repository", displayLabelAdded);
        assertEquals("Afro-American Studies", extensionValue);
        assertEquals("http://id.loc.gov/rwo/agents/no2018062623", valueURI);

        String repositoryTextUnchanged = TestHelpers.getXPath("//*[local-name()='location'][2]/*[local-name()='physicalLocation'][@type = 'repository']", doc);
        assertEquals("xxx", repositoryTextUnchanged);
    }

    @Test
    void mapContentModelToDigitalFormat() throws Exception {
        LibCommMessage lcm = TestHelpers.buildLibCommMessage("mods", "publish-processor-tests-sample-1.xml");

        p.processMessage(lcm);
        Document doc = TestHelpers.extractXmlDoc(lcm);

        String digitalFormat = TestHelpers.getXPath("//*[local-name()='digitalFormat'][1]", doc);

        assertEquals("Books and documents", digitalFormat);
    }
}
