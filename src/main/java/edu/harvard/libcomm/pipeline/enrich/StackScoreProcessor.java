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

/* Add StackScore to MODS records, retrieved from lilCloud API */
public class StackScoreProcessor extends ExternalServiceProcessor implements IProcessor {
	protected Logger log = Logger.getLogger(StackScoreProcessor.class); 	
	
	public void processMessage(LibCommMessage libCommMessage) throws Exception {	

		URI uri = new URI(Config.getInstance().ITEM_URL + "?filter=collection:hollis_catalog&filter=id_inst:(" + getRecordIds(libCommMessage) + ")&fields=shelfrank,id_inst&limit=250");
		process(libCommMessage, uri, "stackscore", "src/main/resources/addstackscore.xsl");

	}

}