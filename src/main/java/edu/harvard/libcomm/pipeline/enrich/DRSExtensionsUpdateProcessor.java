package edu.harvard.libcomm.pipeline.enrich;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLConnection;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.XML;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;
import edu.harvard.libcomm.pipeline.Config;
import edu.harvard.libcomm.pipeline.IProcessor;
import edu.harvard.libcomm.pipeline.MessageUtils;

/* Add Holdings data (physicalLocation, shelfLocator, url) to MODS records, retrieved from lilCloud API */
public class DRSExtensionsUpdateProcessor implements IProcessor {

	protected Logger log = Logger.getLogger(DRSExtensionsProcessor.class);

	public void processMessage(LibCommMessage libCommMessage) throws Exception {

		URI uri = null;
		String urns = libCommMessage.getPayload().getData();
			uri = new URI(Config.getInstance().DRSEXTENSIONS_URL + "?urns=" + urns);
		process(libCommMessage, uri, "results");

	}

	protected void process(LibCommMessage libCommMessage, URI uri, String wrapperToken) throws Exception {

		String data = null;
		String xml = "";
		if (uri == null) {
			xml = "";
		}
		else {
			JSONTokener tokener;
			try {
				URLConnection conn = uri.toURL().openConnection();
				conn.connect();
				InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
				//tokener = new JSONTokener(uri.toURL().openStream());
				tokener = new JSONTokener(isr);
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}

			JSONObject json = new JSONObject(tokener);
			//System.out.println("json: " + json);
			xml = XML.toString(json);
		}
		xml = "<" + wrapperToken + ">" + xml + "</" + wrapperToken + ">";
		//System.out.println("EXT XML: " + xml);
		log.trace("External Service result:" + xml);
/*
		try {
			data = MessageUtils.transformPayloadData(libCommMessage,transformXSL,xml);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
*/
		//System.out.println("DATA: " + data);
		libCommMessage.getPayload().setData(xml);
	}

}
