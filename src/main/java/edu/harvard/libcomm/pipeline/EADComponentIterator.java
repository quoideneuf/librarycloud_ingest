package edu.harvard.libcomm.pipeline;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.UnsupportedOperationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;

public class EADComponentIterator {

    public EADComponentIterator() {
        
    }

    public Iterator<String> getComponents(InputStream is) throws Exception {
    	Document doc = getDocument(is);
    	DOMSource domSource = new DOMSource(doc);
    	NodeList nodes = getNodeList(doc);
    	List<String> componentList = new ArrayList<String>();
    	int count = 0;
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
						throw e;
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
		return componentList.iterator();
    
    }

	private Document getDocument (InputStream is) throws ParserConfigurationException, SAXException, IOException {
	   	Document doc = null;
		DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = builder.parse(is);

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
		return doc;
	}
	
	private NodeList getNodeList (Document doc) throws XPathExpressionException {

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

			NodeList nodes = (NodeList) result;
			return nodes;
	}

    
	private String transformOASIS (DOMSource domSource, String xslFilePath, String xslParam) throws Exception {
		
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
