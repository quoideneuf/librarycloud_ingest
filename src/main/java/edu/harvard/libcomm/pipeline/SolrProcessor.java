package edu.harvard.libcomm.pipeline;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.common.SolrInputDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import edu.harvard.libcomm.message.LibCommMessage;

public class SolrProcessor implements IProcessor {

	@Override
	public void processMessage(LibCommMessage libCommMessage) {
		try {
			String solrXml = MessageUtils.transformPayloadData(libCommMessage,"src/main/resources/mods2solr.xsl");
			populateIndex(solrXml);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void populateIndex(String solrXml) throws Exception {

	    HttpSolrServer server = null;
	    server = SolrServer.getSolrConnection();
		UpdateRequest update = new UpdateRequest();
	    update.add(getSolrInputDocumentList(solrXml));
	    update.process(server);
	    server.commit();
	}

	private List<SolrInputDocument> getSolrInputDocumentList(String solrXml) throws Exception {

	    ArrayList<SolrInputDocument> solrDocList = new ArrayList<SolrInputDocument>();

	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    ByteArrayInputStream bais = new ByteArrayInputStream(solrXml.getBytes("utf-8"));
	    Document doc = dBuilder.parse(new InputSource(bais));

	    NodeList docList = doc.getElementsByTagName("doc");

	    for (int docIdx = 0; docIdx < docList.getLength(); docIdx++) {

	        Node docNode = docList.item(docIdx);

	        if (docNode.getNodeType() == Node.ELEMENT_NODE) {

	            SolrInputDocument solrInputDoc = new SolrInputDocument();

	            Element docElement = (Element) docNode;

	            NodeList fieldsList = docElement.getChildNodes();

	            for (int fieldIdx = 0; fieldIdx < fieldsList.getLength(); fieldIdx++) {

	                Node fieldNode = fieldsList.item(fieldIdx);

	                if (fieldNode.getNodeType() == Node.ELEMENT_NODE) {

	                    Element fieldElement = (Element) fieldNode;

	                    String fieldName = fieldElement.getAttribute("name");
	                    String fieldValue = fieldElement.getTextContent();

	                    solrInputDoc.addField(fieldName, fieldValue);
	                }

	            }

	            solrDocList.add(solrInputDoc);
	        }
	    }

	    return solrDocList;

	}

}
