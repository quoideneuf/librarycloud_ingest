package edu.harvard.libcomm.pipeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileConverter;
import org.apache.commons.compress.utils.IOUtils;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.Record;
import org.w3c.dom.Element;

import edu.harvard.libcomm.message.LibCommMessage;

public class MarcProcessor extends LibCommProcessor {

	public void process(Exchange exchange) throws Exception {	
		
		Message message = exchange.getIn();
		InputStream messageIS = readMessageBody(message);	
		
		libCommMessage = unmarshalMessage(messageIS);
		modifyMessage(libCommMessage);
		
		convertMarcToMarcXML();
		
		String messageString = marshalMessage(libCommMessage);
	    message.setBody(messageString);
	    exchange.setOut(message);
	}


	@Override
	public void modifyMessage(LibCommMessage libComMessage) {
		libCommMessage.setCommand("NORMALIZE");
		
	}

	private void convertMarcToMarcXML() throws FileNotFoundException {
		String filepath = libCommMessage.getPayload().getFilepath();
		InputStream input = null;
		input = new FileInputStream(filepath);
	    MarcReader reader = new MarcStreamReader(input);
	    //MarcWriter writer = new MarcXmlWriter(output, true);
	    MarcWriter writer = null;
	    FileOutputStream output = null;
	    int count = 1;
	    while (reader.hasNext()) {
		    //for testing
		    //if (count > 100) 
		    //	break;
	    	Record record = reader.next();
	        if (count == 1 || count % 25 == 1) {
	        	output = new FileOutputStream(filepath.substring(0, filepath.lastIndexOf("/") + 1) + "marcxml/aleph" + count + ".xml");
	        	writer = new MarcXmlWriter(output, true);
	        }
	    	writer.write(record);
	        if (count % 25 == 0) {
	        	writer.close();
	        }
	        count++;  	
	    }   
	}
	
}
