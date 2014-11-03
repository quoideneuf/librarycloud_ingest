package edu.harvard.libcomm.pipeline;

import org.apache.log4j.Logger;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;

public class ModsProcessor implements IProcessor {
	protected Logger log = Logger.getLogger(ModsProcessor.class); 

	public void processMessage(LibCommMessage libCommMessage) throws Exception {	
		String modsCollection = null;
		libCommMessage.setCommand("ENRICH");
		try {
			modsCollection = MessageUtils.transformPayloadData(libCommMessage,"src/main/resources/MARC21slim2MODS3-5.xsl", null);	
		} catch (Exception e) {
			log.error("Could not transform record from MARC to MODS");
			throw e;
		}	
		log.trace("ModProcessor Result:" + modsCollection);
        Payload payload = new Payload();
        payload.setSource("aleph");
        payload.setFormat("mods");
        payload.setData(modsCollection);
        libCommMessage.setPayload(payload);
	}

	
}
