package edu.harvard.libcomm.pipeline.enrich;

import java.io.IOException;
import java.net.URI;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.XML;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;
import edu.harvard.libcomm.pipeline.IProcessor;
import edu.harvard.libcomm.pipeline.Config;
import edu.harvard.libcomm.pipeline.MessageUtils;

public class HoldingsProcessor implements IProcessor {
	protected Logger log = Logger.getLogger(HoldingsProcessor.class); 	
	
	public void processMessage(LibCommMessage libCommMessage) throws Exception {	
	
		String data = null;
		String recids = "0";

		try {
			recids = MessageUtils.transformPayloadData(libCommMessage,"src/main/resources/recids.xsl",null);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		URI uri = new URI(Config.getInstance().HOLDINGS_URL + "?filter=MarcLKRB:(" + recids + ")&fields=MarcLKRB,Marc852B,Marc856U,DisplayCallNumber&limit=250");
		JSONTokener tokener;
		try {
			tokener = new JSONTokener(uri.toURL().openStream());
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		
		JSONObject holdingsJson = new JSONObject(tokener);
		String holdingsXml = XML.toString(holdingsJson);
		holdingsXml = "<holdings>" + holdingsXml + "</holdings>";
		log.trace("HoldingsProcessor result:" + holdingsXml);
		
		try {
			data = MessageUtils.transformPayloadData(libCommMessage,"src/main/resources/addholdings.xsl",holdingsXml);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		Payload payload = new Payload();
		payload.setData(data);
        libCommMessage.setPayload(payload);
        
	}
}
