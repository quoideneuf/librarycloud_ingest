package edu.harvard.libcomm.pipeline;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;

public class PublishProcessor implements IProcessor {

	@Override
	public void processMessage(LibCommMessage libCommMessage) {
		String data = null;
		libCommMessage.setCommand("PUBLISH");
		try {
			data = MessageUtils.transformPayloadData(libCommMessage,"src/main/resources/remove-mods-restricted.xsl");
		} catch (Exception e) {
			e.printStackTrace();
		}	
	
        Payload payload = new Payload();
        payload.setData(data);
        libCommMessage.setPayload(payload);
		
	}
	
}
