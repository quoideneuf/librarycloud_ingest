package edu.harvard.libcomm.pipeline;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.XML;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;
import gov.loc.mods.v3.ModsCollection;
import gov.loc.mods.v3.ModsType;

public class HoldingsProcessor implements IProcessor {
	
	public void processMessage(LibCommMessage libCommMessage) throws Exception {	
	
		String data = null;
		String recids = null;
		libCommMessage.setCommand("PUBLISH");
		try {
			recids = MessageUtils.transformPayloadData(libCommMessage,"src/main/resources/recids.xsl",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		URI uri = new URI(Config.getInstance().HOLDINGS_URL + "?filter=MarcLKRB:(" + recids + ")&fields=MarcLKRB,Marc852B,Marc856U,DisplayCallNumber&limit=250");
		JSONTokener tokener = new JSONTokener(uri.toURL().openStream());
		JSONObject holdingsJson = new JSONObject(tokener);
		String holdingsXml = XML.toString(holdingsJson);
		holdingsXml = "<holdings>" + holdingsXml + "</holdings>";
		
		try {
			data = MessageUtils.transformPayloadData(libCommMessage,"src/main/resources/addholdings.xsl",holdingsXml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Payload payload = new Payload();
		payload.setData(data);
        libCommMessage.setPayload(payload);
        
	}




}
