package edu.harvard.libcomm.pipeline;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.harvard.libcomm.message.LibCommMessage;


public class EADSplitter implements ISplitter {
	protected Logger log = Logger.getLogger(EADSplitter.class); 	

	public Iterator getIterator(InputStream is) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, JAXBException {
		EADComponentIterator helper = new EADComponentIterator();
		EADComponentIterator eadComponentIterator = new EADComponentIterator();
	    return eadComponentIterator.getComponents(is);
	}


}