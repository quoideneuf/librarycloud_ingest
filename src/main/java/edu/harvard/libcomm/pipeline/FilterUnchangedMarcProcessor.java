package edu.harvard.libcomm.pipeline;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;
import org.apache.log4j.Logger;
import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;

import gov.loc.marc.CollectionType;
import gov.loc.mods.v3.ModsCollection;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.dao.IngestDAO;

/* Given a message that contains a raw MARC record (not wrapped in a LibCommMessage),
   extract the record ID from a header ("recordFileName") on the exchange. Use that ID to lookup the
   checksum of the record in a database, and compare with the checksum of the current
   message. If they differ, empty the contents of the message */
public class FilterUnchangedMarcProcessor  extends LibCommProcessor implements Processor {

	protected Logger log = Logger.getLogger(FilterUnchangedMarcProcessor.class); 

	@Autowired
	private IngestDAO ingestDao;

	/**
	 * Invoked by Camel to process the message 
	 * @param  exchange
	 * @throws Exception
	 */
	public synchronized void process(Exchange exchange) throws Exception {	
		
		/* Get the contents of the message */
		JAXBContext context = initContext();
		Message message = exchange.getIn();
		InputStream messageIS = MessageUtils.readMessageBody(message);

		/* Extract the ID of the message, set by a header in the camel-context.xml */
		String hashId = "marc-" + message.getHeader("recordFileName");

		/* Calculate the message checksum as an integer*/
		Integer checksum = IOUtils.toString(messageIS, "UTF-8").hashCode();

		Map<String, Integer> recordMap = new HashMap<String, Integer>();
		recordMap.put(hashId, checksum);
		Set<String> duplicateRecordIds = ingestDao.checkAndSaveItemChecksum(recordMap);

		if (duplicateRecordIds.size() == 1) {
		    message.setBody("");
		}

	    exchange.setOut(message);
	}
	
}
