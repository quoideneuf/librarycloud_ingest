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
			throw new Error("No processor defined for message");
		}

		Message message = exchange.getIn();
		InputStream messageIS = readMessageBody(message);	
		
		libCommMessage = MessageUtils.unmarshalLibCommMessage(messageIS);
		processor.processMessage(libCommMessage);
		
		String messageString = MessageUtils.marshalMessage(libCommMessage);
	    message.setBody(messageString);
	    exchange.setOut(message);
	}

	protected InputStream readMessageBody (Message message) throws FileNotFoundException, UnsupportedEncodingException {
		Object body = message.getBody(); 
		InputStream messageIS = null; 
		
		if (body instanceof GenericFile) { 
			GenericFile<File> file = (GenericFile<File>) body; 
			try {
				messageIS = new FileInputStream(file.getFile());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw e;
			}
		} else if (body instanceof String) {
			try {
				messageIS = new ByteArrayInputStream(((String)body).getBytes("UTF-8"));	
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw e;
			}			
		}
		return messageIS;
	}
	
	public void setProcessor(IProcessor p) {
		this.processor = p;
	}		

	public IProcessor getProcessor() {
		return this.processor;
	}
}
