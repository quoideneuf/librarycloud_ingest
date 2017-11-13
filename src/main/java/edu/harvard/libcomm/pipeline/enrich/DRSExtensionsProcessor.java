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
import edu.harvard.libcomm.pipeline.Config;
import edu.harvard.libcomm.pipeline.IProcessor;
import edu.harvard.libcomm.pipeline.MessageUtils;

/* Add Holdings data (physicalLocation, shelfLocator, url) to MODS records, retrieved from lilCloud API */
public class DRSExtensionsProcessor extends ExternalServiceProcessor implements IProcessor {

	protected Logger log = Logger.getLogger(DRSExtensionsProcessor.class);

	public void processMessage(LibCommMessage libCommMessage) throws Exception {	
	
		URI uri = null;
		String urns = getUrns(libCommMessage);
		//urns = urns.startsWith("OR") ? urns.substring(2) : urns;
		urns = urns.endsWith(" OR ") ? urns.substring(0, urns.length() - 4) : urns;
		urns = "(" + urns.replace(" ","+") + ")";
		if (urns.equals("") || urns == null || urns.contains("ebook"))
			uri = null;
		else
			uri = new URI(Config.getInstance().SOLR_EXTENSIONS_URL + "/select?q=urn:" + urns);
		//System.out.println(uri.toString());
		process(libCommMessage, uri, "results", "src/main/resources/adddrsextensions.xsl");
        
	}
}
