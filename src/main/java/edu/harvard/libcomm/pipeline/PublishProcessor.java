package edu.harvard.libcomm.pipeline;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;

public class PublishProcessor implements IProcessor {

	@Override
	public void processMessage(LibCommMessage libCommMessage) throws Exception {
		String data = null;
		libCommMessage.setCommand("PUBLISH");
		String modsCount = null;
		try {

			data = MessageUtils.transformPayloadData(libCommMessage,"src/main/resources/remove-mods-restricted.xsl",null);
			LibCommMessage tempMessage = new LibCommMessage();
			Payload tempPayload = new Payload();
			tempPayload.setData(data);
			tempMessage.setPayload(tempPayload);
			modsCount = MessageUtils.transformPayloadData(tempMessage, "src/main/resources/recids-count.xsl", null);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		if(modsCount == null || modsCount.equals("") || modsCount.equals("1")) {
			//empty data set
			data = "";
		}

		Payload payload = new Payload();
        payload.setData(data);
        libCommMessage.setPayload(payload);
		
	}


}
