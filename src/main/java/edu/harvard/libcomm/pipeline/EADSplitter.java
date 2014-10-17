package edu.harvard.libcomm.pipeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import java.util.ArrayList;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;


public class EADSplitter  {

	public List<String> splitEADFiles(String body) throws JAXBException,ParserConfigurationException, SAXException, IOException, XPathExpressionException  {

		LibCommMessage libCommMessage = MessageUtils.unmarshalLibCommMessage(new StringReader(body));
		
		if ((libCommMessage != null) && (libCommMessage.getPayload() != null)) {

			String directorypath = libCommMessage.getPayload().getFilepath();
	        File dir = new File(directorypath);
	        //TO DO - too memory intensive, use Files.walkFileTree instead
	        File[] directoryListing = dir.listFiles();
	        List<String> componentList = new ArrayList<String>();
	        for (File child : directoryListing) {
	        	Document doc = null;
	 			DocumentBuilder builder;
	 			try {
	 				builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	 				doc = builder.parse(child);
	 	
	 			} catch (ParserConfigurationException e) {
	 				e.printStackTrace();
	 				throw e;
	 			} catch (SAXException e) {
	 				e.printStackTrace();
	 				throw e;
	 			} catch (IOException e) {
	 				e.printStackTrace();
	 				throw e;
	 			}
	     		
	 			XPathFactory factory = XPathFactory.newInstance();
	 			XPath xpath = factory.newXPath();
	   			Object result = null;
	 			XPathExpression componentId = null;
	 			try {
	 				componentId = xpath.compile("//c/@id");
	 				result = componentId.evaluate(doc, XPathConstants.NODESET);
	 			} catch (XPathExpressionException e) {
	 				e.printStackTrace();
	 				throw e;
	 			}

	 			DOMSource domSource = new DOMSource(doc);
	   			NodeList nodes = (NodeList) result;
	   			if (nodes.getLength() > 0) {
	   				for (int i = 0; i < nodes.getLength(); i++) {
	   			        String nodeName = nodes.item(i).getNodeName();
	   			        String nodeValue = nodes.item(i).getNodeValue();
	   			        if (nodeName.equals("id")) {
	   			        	String eadComponentMods = null;
	 						try {
	 							eadComponentMods = transformOASIS(domSource, "src/main/resources/eadcomponent2mods.xsl", nodeValue);
	 						} catch (Exception e) {
	 							e.printStackTrace();
	 						}
	 						LibCommMessage lcmessage = new LibCommMessage();
	 						Payload payload = new Payload();
	 						payload.setFormat("MODS");
	 						payload.setSource("OASIS");
	 						payload.setData(eadComponentMods);
	 						lcmessage.setCommand("PUBLISH");
	   			        	lcmessage.setPayload(payload);
	   			        	componentList.add(MessageUtils.marshalMessage(lcmessage));
	   			        }
	   				}
	   			}
	        }
	        
		    return componentList;
		} else {
			System.out.println("LOGGING TO DO - Something bad happenned in parsing incoming message");
			throw new RuntimeException("Error parsing payload");
		}

	}

	static protected String transformOASIS (DOMSource domSource, String xslFilePath, String xslParam) throws Exception {
		
		StringWriter writer = new StringWriter();
		final InputStream xsl = new FileInputStream(xslFilePath);
		final TransformerFactory tFactory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl",null);
        StreamSource styleSource = new StreamSource(xsl);
        Transformer transformer = tFactory.newTransformer(styleSource);
       	transformer.setParameter("componentid", xslParam);
        StreamResult result = new StreamResult(writer);
        transformer.transform(domSource, result);
        return writer.toString();
	}
	
}