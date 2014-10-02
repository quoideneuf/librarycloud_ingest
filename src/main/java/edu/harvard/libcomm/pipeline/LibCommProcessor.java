package edu.harvard.libcomm.pipeline;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import javax.xml.bind.JAXBException;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;

import edu.harvard.libcomm.message.LibCommMessage;
import gov.loc.marc.CollectionType;
import gov.loc.mods.v3.ModsCollection;

public class LibCommProcessor implements Processor {

	protected LibCommMessage libCommMessage = null;
	protected ModsCollection modsCollection = null;
	protected CollectionType collectionType = null;
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
		
		libCommMessage = unmarshalLibCommMessage(messageIS);

		processor.processMessage(libCommMessage);
		
		String messageString = marshalMessage(libCommMessage);
	    message.setBody(messageString);
	    exchange.setOut(message);
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
		} else if (body instanceof String) {
			try {
				messageIS = new ByteArrayInputStream(((String)body).getBytes("UTF-8"));	
			} catch (java.io.UnsupportedEncodingException e) {
				e.printStackTrace();
			}			
		}
		return messageIS;
	}
	
	protected LibCommMessage unmarshalLibCommMessage (InputStream messageIS) throws JAXBException {
		return MessageUtils.unmarshalLibCommMessage(messageIS);
	}
	
	protected ModsCollection unmarshalMods (StringReader reader) throws JAXBException {
		return modsCollection = MessageUtils.unmarshalMods(reader);
	}

	protected CollectionType unmarshalMarc (StringReader reader) {
		return collectionType = MessageUtils.unmarshalMarc(reader);
	}
	
	protected String marshalMessage (LibCommMessage libCommMessage) throws JAXBException {
		return MessageUtils.marshalMessage(libCommMessage);
	}

	public void setProcessor(IProcessor p) {
		this.processor = p;
	}		

	public IProcessor getProcessor() {
		return this.processor;
	}
}
