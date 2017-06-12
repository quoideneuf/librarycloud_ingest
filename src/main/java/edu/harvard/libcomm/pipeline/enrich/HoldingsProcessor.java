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
public class HoldingsProcessor extends ExternalXMLServiceProcessor implements IProcessor {

	protected Logger log = Logger.getLogger(HoldingsProcessor.class); 	

	public void processMessage(LibCommMessage libCommMessage) throws Exception {	
	
		//URI uri = new URI(Config.getInstance().HOLDINGS_URL + "?filter=MarcLKRB:(" + getRecordIds(libCommMessage) + ")&fields=MarcLKRB,Marc852B,Marc856U,DisplayCallNumber&limit=250");
		URI uri = new URI(Config.getInstance().SOLR_HOLDINGS_URL + "/select?q=004:(" + getRecordIds(libCommMessage) + ")&rows=250");

		process(libCommMessage, uri, "holdings", "src/main/resources/addholdings.xsl");

	}
}
