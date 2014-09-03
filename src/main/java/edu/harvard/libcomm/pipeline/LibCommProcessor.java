package edu.harvard.libcomm.pipeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;

import edu.harvard.libcomm.message.LibCommMessage;
import gov.loc.mods.v3.ModsCollection;

public abstract class LibCommProcessor implements Processor {

	protected LibCommMessage libCommMessage = null;
	protected ModsCollection modsCollection = null;
	
	protected InputStream readMessageBody (Message message) {
		Object body = message.getBody(); 
		InputStream messageIS = null; 
		
		if (body instanceof GenericFile) { 
			GenericFile<File> file = (GenericFile<File>) body; 
			try {
				messageIS = new FileInputStream(file.getFile());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return messageIS;
	}
	
	protected LibCommMessage unmarshalMessage (InputStream messageIS) {
		return MessageUtils.unmarshalMessage(messageIS);
	}
	
	protected ModsCollection unmarshalMessage (StringReader reader) {
		return modsCollection = MessageUtils.unmarshalMessage(reader);
	}

	protected String marshalMessage (LibCommMessage libCommMessage) {
		return MessageUtils.marshalMessage(libCommMessage);
	}
	
	public abstract void modifyMessage(LibCommMessage libComMessage);
	
	
}
