package edu.harvard.libcomm.pipeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import org.apache.commons.compress.utils.IOUtils;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.Record;
import org.w3c.dom.Element;

import edu.harvard.libcomm.message.LibCommMessage;

public class MarcProcessor implements IProcessor {

	public void processMessage(LibCommMessage libCommMessage) {		
		libCommMessage.setCommand("NORMALIZE");
		try {
			convertMarcToMarcXML(libCommMessage);	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}

	private void convertMarcToMarcXML(LibCommMessage libCommMessage) throws FileNotFoundException {
		String filepath = libCommMessage.getPayload().getFilepath();
		InputStream input = null;
		input = new FileInputStream(filepath);
	    MarcReader reader = new MarcStreamReader(input);
	    MarcWriter writer = null;
	    FileOutputStream output = null;
	    int count = 1;
	    while (reader.hasNext()) {
	    	try {
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
	    	} catch (org.marc4j.MarcException ex) {
	    		ex.printStackTrace();
	    	}
	    }   
	}
	
}
