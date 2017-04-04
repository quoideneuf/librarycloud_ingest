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
import edu.harvard.libcomm.pipeline.MessageUtils;
import edu.harvard.libcomm.pipeline.IProcessor;

/* Add LCC Subject Headings as subjects, based on call number, using data from lilCloud API */
public class LCCProcessor extends ExternalServiceProcessor implements IProcessor {
	protected Logger log = Logger.getLogger(LCCProcessor.class); 	
	
	public void processMessage(LibCommMessage libCommMessage) throws Exception {	
	
		URI uri = new URI(Config.getInstance().ITEM_URL + "?filter=collection:hollis_catalog&filter=id_inst:(" + getRecordIds(libCommMessage) + ")&fields=loc_call_num_subject,id_inst&limit=250");
		process(libCommMessage, uri, "lcc", "src/main/resources/addlcc.xsl");

	}

}