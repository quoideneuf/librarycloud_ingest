package edu.harvard.libcomm.pipeline;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;

public class CollectionUpdateProcessor implements IProcessor {
	protected Logger log = Logger.getLogger(CollectionsProcessor.class); 	
	
	public void processMessage(LibCommMessage libCommMessage) throws Exception {	
		String itemId;
		String data;
		String collectionData;

		collectionData = libCommMessage.getPayload().getData();

		itemId = MessageUtils.transformPayloadData(libCommMessage, "src/main/resources/item_id.xsl", null);
		if (itemId == null || itemId.length() == 0){
			Payload payload = new Payload();
			payload.setData("");
			libCommMessage.setPayload(payload);
			return;
		}

		data = getSolrModsRecord(itemId);
		LibCommMessage temporaryMessage = new LibCommMessage();
		Payload temporaryPayload = new Payload();
		temporaryPayload.setData(data);
		temporaryMessage.setPayload(temporaryPayload);

		data = MessageUtils.transformPayloadData(temporaryMessage, "src/main/resources/addcollections.xsl", collectionData);

		Payload payload = new Payload();
		payload.setData(data);

        libCommMessage.setPayload(payload);
        
	}

	private String getSolrModsRecord(String itemId)
	{
		String modsRecord = "";

		SolrDocumentList docs;
		SolrDocument doc = null;
		HttpSolrServer server = null;
		try {
			server = SolrServer.getSolrConnection();
			SolrQuery query = new SolrQuery("recordIdentifier:" + itemId);
			QueryResponse response = server.query(query);
			docs = response.getResults();
			if (docs.size() == 0)
				log.debug("Item " + itemId + " not found");
			else {
				doc = docs.get(0);

			}
		}
		catch (SolrServerException  se) {
			se.printStackTrace();
			log.error(se.getMessage());
		}

		modsRecord = doc.getFieldValue("originalMods").toString();
		return modsRecord;
	}

}
