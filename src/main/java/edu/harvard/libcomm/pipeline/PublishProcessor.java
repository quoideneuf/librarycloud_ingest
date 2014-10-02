package edu.harvard.libcomm.pipeline;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;
import gov.loc.mods.v3.ModsCollection;
import gov.loc.mods.v3.ModsType;
import gov.loc.mods.v3.RecordInfoType;

public class PublishProcessor implements IProcessor {

	@Override
	public void processMessage(LibCommMessage libCommMessage) throws Exception {
		String data = null;
		libCommMessage.setCommand("PUBLISH");
		try {
			data = MessageUtils.transformPayloadData(libCommMessage,"src/main/resources/remove-mods-restricted.xsl",null);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
	
        Payload payload = new Payload();
        payload.setData(data);
        libCommMessage.setPayload(payload);
		
	}
	
}
