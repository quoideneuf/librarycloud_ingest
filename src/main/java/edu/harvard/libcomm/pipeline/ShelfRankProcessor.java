package edu.harvard.libcomm.pipeline;


import java.io.IOException;
import java.net.URI;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.XML;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;

public class ShelfRankProcessor implements IProcessor {
	protected Logger log = Logger.getLogger(ShelfRankProcessor.class); 	
	
	public void processMessage(LibCommMessage libCommMessage) throws Exception {	
	
		String data = null;
		String recids = null;
		libCommMessage.setCommand("PUBLISH");
		try {
			recids = MessageUtils.transformPayloadData(libCommMessage,"src/main/resources/recids.xsl",null);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		//System.out.println("RECIDS: " + recids);
		URI uri = new URI(Config.getInstance().ITEMS_URL + "?filter=hollis_catalog&filter=id_inst:(" + recids + ")&fields=shelfrank,id_inst&limit=250");
		JSONTokener tokener;
		try {
			Date start = new Date();
			tokener = new JSONTokener(uri.toURL().openStream());
			Date end = new Date();
			log.debug("ShelfRankProcesser query time: " + (end.getTime() - start.getTime()));
			log.trace("ShelfRankProcesser query : " +  uri.toURL());
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		
		JSONObject shelfrankJson = new JSONObject(tokener);
		String shelfrankXml = XML.toString(shelfrankJson);
		System.out.println(shelfrankXml);
		shelfrankXml = "<shelfrank>" + shelfrankXml + "</shelfrank>";
		log.trace("ShelfRankProcessor result:" + shelfrankXml);
		
		try {
			data = MessageUtils.transformPayloadData(libCommMessage,"src/main/resources/addshelfrank.xsl",shelfrankXml);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		Payload payload = new Payload();
		payload.setData(data);
        libCommMessage.setPayload(payload);
        
	}

}