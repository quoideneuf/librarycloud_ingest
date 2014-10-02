package edu.harvard.libcomm.pipeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import java.util.ArrayList;

import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;

import edu.harvard.libcomm.message.LibCommMessage;


public class MarcSplitter  {

	public MarcFileIterator splitMarcFile(String body) throws FileNotFoundException, JAXBException  {

		LibCommMessage libCommMessage = MessageUtils.unmarshalLibCommMessage(new StringReader(body));

		if ((libCommMessage != null) && (libCommMessage.getPayload() != null)) {
			String filepath = libCommMessage.getPayload().getFilepath();
			InputStream input = null;

			try {
				input = new FileInputStream(filepath);			
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
				throw ex;
			}		
		    MarcReader reader = new MarcStreamReader(input);
		    return new MarcFileIterator(reader);
		} else {
			System.out.println("LOGGING TO DO - Something bad happenned in parsing incoming message");
			throw new RuntimeException("Error parsing payload");
		}
	}

}