package edu.harvard.libcomm.pipeline.enrich;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.pipeline.Config;
import edu.harvard.libcomm.pipeline.IProcessor;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.XML;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLConnection;

/* Add Holdings data (physicalLocation, shelfLocator, url) to MODS records, retrieved from lilCloud API */
public class DRSExtensionsNewUpdateProcessor implements IProcessor {

	protected Logger log = Logger.getLogger(DRSExtensionsProcessor.class);

	public void processMessage(LibCommMessage libCommMessage) throws Exception {

		URI uri = null;
		String json = libCommMessage.getPayload().getData();
			//uri = new URI(Config.getInstance().DRSEXTENSIONS_URL + "?urns=" + urns);
		process(libCommMessage, json, "results");

	}

	protected void process(LibCommMessage libCommMessage, String json, String wrapperToken) throws Exception {

		String xml = "";

		JSONTokener tokener;
		try {
			tokener = new JSONTokener(json);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		JSONObject jsonObj = new JSONObject(tokener);
		//System.out.println("json: " + json);
		xml = XML.toString(jsonObj);
		xml = "<" + wrapperToken + ">" + xml + "</" + wrapperToken + ">";
		//System.out.println("EXT XML: " + xml);
		log.trace("External Service result:" + xml);
		libCommMessage.getPayload().setData(xml);
	}

}
