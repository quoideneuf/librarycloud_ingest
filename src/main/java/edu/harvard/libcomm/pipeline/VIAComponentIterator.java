package edu.harvard.libcomm.pipeline;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import javax.xml.namespace.NamespaceContext;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;

public class VIAComponentIterator {

public VIAComponentIterator() {
        
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
		        String urn = nodeValue.split("urn-3:")[1];
		        //System.out.println("urn: " + urn);
	        	String viaComponentMods = null;
				try {
					viaComponentMods = transformVIA(domSource, "src/main/resources/viacomponent2mods.xsl", urn);
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
				//System.out.print("VCM: " + viaComponentMods);
				LibCommMessage lcmessage = new LibCommMessage();
				Payload payload = new Payload();
				payload.setFormat("MODS");
				payload.setSource("VIA");
				payload.setData(viaComponentMods);
				lcmessage.setCommand("PUBLISH");
	        	lcmessage.setPayload(payload);
	        	componentList.add(MessageUtils.marshalMessage(lcmessage));
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
			
			
			xpath.setNamespaceContext(new NamespaceContext() {
			    public String getNamespaceURI(String prefix) {
			        return prefix.equals("xlink") ? "http://www.w3.org/TR/xlink"  : null;
			    }

			    public Iterator<?> getPrefixes(String val) {
			        return null;
			    }

			    public String getPrefix(String uri) {
			        return null;
			    }
			});
			
			Object result = null;
			XPathExpression componentId = null;
			try {
				componentId = xpath.compile("//image/@xlink:href");
				result = componentId.evaluate(doc, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
				throw e;
			}

			NodeList nodes = (NodeList) result;
			return nodes;
	}

    
	private String transformVIA (DOMSource domSource, String xslFilePath, String xslParam) throws Exception {
		
		StringWriter writer = new StringWriter();
		final InputStream xsl = new FileInputStream(xslFilePath);
		final TransformerFactory tFactory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl",null);
        StreamSource styleSource = new StreamSource(xsl);
        Transformer transformer = tFactory.newTransformer(styleSource);
       	transformer.setParameter("urn", xslParam);
        StreamResult result = new StreamResult(writer);
        transformer.transform(domSource, result);
        return writer.toString();
	}
}	
