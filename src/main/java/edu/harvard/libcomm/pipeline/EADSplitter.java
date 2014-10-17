package edu.harvard.libcomm.pipeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;


public class EADSplitter  {

	public List<String> splitEADFiles(String body) throws JAXBException  {

		LibCommMessage libCommMessage = MessageUtils.unmarshalLibCommMessage(new StringReader(body));
		
		if ((libCommMessage != null) && (libCommMessage.getPayload() != null)) {

			String directorypath = libCommMessage.getPayload().getFilepath();
	        File dir = new File(directorypath);
	        File[] directoryListing = dir.listFiles();
	        List<String> componentList = new ArrayList<String>();
	        for (File child : directoryListing) {
	        	Document doc = null;
	 			DocumentBuilder builder;
	 			try {
	 				builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	 				doc = builder.parse(child);
	 	
	 			} catch (ParserConfigurationException e1) {
	 				// TODO Auto-generated catch block
	 				e1.printStackTrace();
	 			} catch (SAXException e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			} catch (IOException e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			}
	     		
	       		//XPath xpath = getXPath(child);
	 			XPathFactory factory = XPathFactory.newInstance();
	 			XPath xpath = factory.newXPath();

	   			//XPathExpression images = xpath.compile("//Assets/Asset[@size='DRS_full']/@uri");
	   			XPathExpression componentId = null;
	 			try {
	 				componentId = xpath.compile("//c/@id");
	 			} catch (XPathExpressionException e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			}
	   			Object result = null;
	 			try {
	 				result = componentId.evaluate(doc, XPathConstants.NODESET);
	 			} catch (XPathExpressionException e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			}
	 			DOMSource domSource = new DOMSource(doc);
	   			NodeList nodes = (NodeList) result;
	   			if (nodes.getLength() > 0) {
	   				for (int i = 0; i < nodes.getLength(); i++) {
	   			        String nodeName = nodes.item(i).getNodeName();
	   			        String nodeValue = nodes.item(i).getNodeValue();
	   			        if (nodeName.equals("id")) {
	   			        	System.out.println(nodeName + ":" + nodeValue);
	   			        	String eadComponentMods = null;
	 						try {
	 							eadComponentMods = transformOASIS(domSource, "src/main/resources/eadcomponent2mods.xsl", nodeValue);
	 							System.out.println("COMP: " + eadComponentMods);
	 						} catch (Exception e) {
	 							// TODO Auto-generated catch block
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

        //final TransformerFactory tFactory = TransformerFactory.newInstance();
		final TransformerFactory tFactory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl",null);
				
        StreamSource styleSource = new StreamSource(xsl);
        Transformer transformer = tFactory.newTransformer(styleSource);
        
        if (xslParam == null)
        	System.out.println();
        else
        	transformer.setParameter("componentid", xslParam);

        StreamResult result = new StreamResult(writer);
        transformer.transform(domSource, result);
        //System.out.println(writer.toString());
        return writer.toString();
	}
	
}