package edu.harvard.libcomm.pipeline;

import edu.harvard.libcomm.message.LibCommMessage;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Iterator;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;


/**
 * This version of the Splitter does not assume that there's an existing LibComMessage
 * command with a reference to a file to be processed - it operates on raw data that
 * isn't yet structured into a message
 */
public class MessageSplitter extends Splitter {
	protected Logger log = Logger.getLogger(RawSplitter.class); 	

	@Override
	protected InputStream getResourceInputStream(String body) throws FileNotFoundException, IOException, JAXBException  {
		LibCommMessage libCommMessage = MessageUtils.unmarshalLibCommMessage(new StringReader(body));
		String resource = null;
		if ((libCommMessage != null) && (libCommMessage.getPayload() != null)) {
			resource = libCommMessage.getPayload().getData();
			return new ByteArrayInputStream(resource.getBytes("UTF-8"));
		} else {
			System.out.println("LOGGING TO DO - Something bad happenned in parsing incoming message");
			throw new RuntimeException("Error parsing payload");
		}
	}

}