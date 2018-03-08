package edu.harvard.libcomm.pipeline;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;
import org.apache.log4j.Logger;

import edu.harvard.libcomm.message.LibCommMessage;
import gov.loc.marc.CollectionType;
import gov.loc.mods.v3.ModsCollection;

public class ExtractPayloadProcessor  extends LibCommProcessor implements Processor {

	protected Logger log = Logger.getLogger(ExtractPayloadProcessor.class); 
	protected LibCommMessage libCommMessage = null;

	/**
	 * Invoked by Camel to process the message 
	 * @param  exchange
	 * @throws Exception
	 */
	public synchronized void process(Exchange exchange) throws Exception {	
		
		JAXBContext context = initContext();
		Message message = exchange.getIn();
		InputStream messageIS = MessageUtils.readMessageBody(message);			
		libCommMessage = unmarshalMessage(context, messageIS);
	    message.setBody(libCommMessage.getPayload().getData());
	    exchange.setOut(message);
	}
	
}
