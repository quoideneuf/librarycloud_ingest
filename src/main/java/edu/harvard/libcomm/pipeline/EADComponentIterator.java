package edu.harvard.libcomm.pipeline;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.UnsupportedOperationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

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

public class EADComponentIterator implements Iterator<String> {

	private EADReader eadReader;
	private NodeList nodes;
	private DOMSource domSource;
	private int position = 0;

    public EADComponentIterator(EADReader reader) throws Exception {
        this.eadReader = reader;
        nodes = reader.getNodes();
        domSource = reader.getDOMSource();        	
    }

    @Override
    public boolean hasNext() {
    	return ((nodes != null) && (position < nodes.getLength()));
    }

    @Override
    public String next() {
    	while ((nodes != null) && (position < nodes.getLength())) {
	        String nodeName = nodes.item(position).getNodeName();
	        String nodeValue = nodes.item(position).getNodeValue();
	        position++;
	        if (nodeName.equals("id")) {
	        	String eadComponentMods = null;
				try {
					eadComponentMods = transformOASIS(domSource, "src/main/resources/eadcomponent2mods.xsl", nodeValue);
				} catch (Exception e) {
					e.printStackTrace();
					throw new NoSuchElementException();
				}
				LibCommMessage lcmessage = new LibCommMessage();
				Payload payload = new Payload();
				payload.setFormat("MODS");
				payload.setSource("OASIS");
				payload.setData(eadComponentMods);
				lcmessage.setCommand("ENRICH");
	        	lcmessage.setPayload(payload);
	        	try {
	        		return MessageUtils.marshalMessage(lcmessage);
				} catch (JAXBException e) {
					e.printStackTrace();
					return null;
				}		        		
	        }
    	}
    	throw new NoSuchElementException();
	}

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
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
