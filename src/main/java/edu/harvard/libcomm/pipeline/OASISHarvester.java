package edu.harvard.libcomm.pipeline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class OASISHarvester {

	public static void main(String args[]) {
		URI allEadidsUri = null;
		try {
			allEadidsUri = new URI(Config.getInstance().OASIS_URL + "/deliver/allFindingAidsXML");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = null;
	    Document doc = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	    try {
			doc = db.parse(allEadidsUri.toString());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		NodeList nodeList = null;
		try {
			nodeList = (NodeList) xpath.compile("//a").evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < nodeList.getLength(); i++) {
			String eadid = nodeList.item(i).getFirstChild().getNodeValue();
			URI componentIdUri = null;
			try {
				componentIdUri = new URI(Config.getInstance().OASIS_URL + "/harvestlistdig/" + eadid + ".tsv");
		        BufferedReader in = new BufferedReader(new InputStreamReader(componentIdUri.toURL().openStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                	String componentId = inputLine.split("\t")[1];
                	URI componentXmlUri = new URI(Config.getInstance().OASIS_URL + "/component/" + componentId + ".xml");
                	BufferedReader xmlIn = new BufferedReader(new InputStreamReader(componentXmlUri.toURL().openStream()));
            		FileWriter fw = new FileWriter(new File("/temp/oasis/" + componentId + ".xml"));
            		String xmlInputLine = null;
            		while ((xmlInputLine = xmlIn.readLine()) != null) {
            			fw.write(xmlInputLine);
            		}
            		xmlIn.close();
            		fw.close();                	
                }	
                in.close();
			
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    //System.out.println(eadid);
			catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			

	}
	
}
