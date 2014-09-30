package edu.harvard.libcomm.pipeline;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import edu.harvard.libcomm.message.LibCommMessage;
import gov.loc.mods.v3.ModsCollection;
import gov.loc.mods.v3.ModsType;

public class HoldingsProcessor implements IProcessor {
	
	public void processMessage(LibCommMessage libCommMessage) throws Exception {	
		String data = libCommMessage.getPayload().getData();
		System.out.println(data);
		StringReader modsReader = new StringReader(data);
		ModsCollection modsCollection = MessageUtils.unmarshalMods(modsReader);
		List<ModsType> modsList =  modsCollection.getMods();
        Iterator<ModsType> modsIterator = modsList.iterator();
        while (modsIterator.hasNext()) {
           List<Object> modsGroup = modsIterator.next().getModsGroup();
           //System.out.println(modsGroup.get(0).toString());
        }
		
	}
}
