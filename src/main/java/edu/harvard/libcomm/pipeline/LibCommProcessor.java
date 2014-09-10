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

public class LibCommProcessor implements Processor {

	protected LibCommMessage libCommMessage = null;
	protected ModsCollection modsCollection = null;
	private IProcessor processor;

	/**
	 * Invoked by Camel to process the message 
	 * @param  exchange
	 * @throws Exception
	 */
	public void process(Exchange exchange) throws Exception {	
		
		if (null == processor) {
			throw new Exception("No processor defined for message");
		}

		Message message = exchange.getIn();
		InputStream messageIS = readMessageBody(message);	
		
		libCommMessage = unmarshalMessage(messageIS);

		processor.processMessage(libCommMessage);
		
		String messageString = marshalMessage(libCommMessage);
	    message.setBody(messageString);
	    exchange.setOut(message);
	}

	/**
	 * Overriden by child classes to do the actual processing of the message.
	 * Default implementation does nothing
	 * @param message
	 */
	public void processMessage (LibCommMessage message) {
		return;
	}
	
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

	public void setProcessor(IProcessor p) {
		this.processor = p;
	}		

	public IProcessor getProcessor() {
		return this.processor;
	}
}
