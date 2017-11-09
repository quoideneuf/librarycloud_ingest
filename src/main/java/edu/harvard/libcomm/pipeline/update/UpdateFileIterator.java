package edu.harvard.libcomm.pipeline.update;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;
import edu.harvard.libcomm.pipeline.MessageUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UpdateFileIterator {
	protected Logger log = Logger.getLogger(UpdateFileIterator.class);
	
    public Iterator<String> getUpdateUrns(InputStream is) throws Exception {
    	BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
    	List<String> urnList = new ArrayList<String>();
    	String urns = null;
    	while((urns = br.readLine()) != null){
    		LibCommMessage lcmessage = new LibCommMessage();
			Payload payload = new Payload();
			payload.setFormat("UPDATE_EXTENSIONS");
			payload.setSource("UPDATE");
			payload.setData(urns);
			lcmessage.setCommand("UPDATE");
        	lcmessage.setPayload(payload);
        	urnList.add(MessageUtils.marshalMessage(lcmessage));
		}
		return urnList.iterator();
    
    }

}
