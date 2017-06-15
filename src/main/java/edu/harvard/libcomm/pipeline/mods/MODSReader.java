package edu.harvard.libcomm.pipeline.mods;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MODSReader {

	private InputStream is;
	private NodeList nodes;
	private DOMSource domSource;	

    public MODSReader(InputStream is) {
		this.is = is;	        
    }

    public NodeList getNodes() throws ParserConfigurationException, XPathExpressionException, ParserConfigurationException, SAXException, IOException {
    	Document doc = getDocument(is);
    	this.domSource = new DOMSource(doc);
    	NodeList nodes = getNodeList(doc);
    	return nodes;
    }

    public DOMSource getDOMSource() throws ParserConfigurationException, SAXException, IOException {
    	if (this.domSource == null) {
	    	Document doc = getDocument(is);
    		this.domSource = new DOMSource(doc);
    	}
		return this.domSource;
    }

	private Document getDocument (InputStream is) throws ParserConfigurationException, SAXException, IOException {
//System.out.println(IOUtils.toString(is,"UTF-8"));
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
			        return prefix.equals("mods") ? "http://www.loc.gov/mods/v3"  : null;
			    }

			    public Iterator<?> getPrefixes(String val) {
			        return null;
			    }

			    public String getPrefix(String uri) {
			        return null;
			    }
			});
			
			Object result = null;
			XPathExpression urns = null;
			try {
				urns = xpath.compile("//mods:url[@access='raw object']"); //and contains(.,'urn-3')");
				result = urns.evaluate(doc, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
				throw e;
			}

			NodeList nodes = (NodeList) result;
			return nodes;
	}

}
