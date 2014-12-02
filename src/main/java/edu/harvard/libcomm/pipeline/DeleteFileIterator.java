package edu.harvard.libcomm.pipeline;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.UpdateRequest;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;

public class DeleteFileIterator {
	protected Logger log = Logger.getLogger(DeleteFileIterator.class); 
	
    public Iterator<String> getDeleteIds(InputStream is) throws Exception {
    	BufferedReader br = new BufferedReader(new InputStreamReader(is));
    	List<String> idList = new ArrayList<String>();
    	String id = null;
    	while((id = br.readLine()) != null){
    		LibCommMessage lcmessage = new LibCommMessage();
			Payload payload = new Payload();
			payload.setFormat("DELETE_ID");
			payload.setSource("DELETE");
			payload.setData(id);
			lcmessage.setCommand("DELETE");
        	lcmessage.setPayload(payload);
        	idList.add(MessageUtils.marshalMessage(lcmessage));
		}
		return idList.iterator();
    
    }

}
