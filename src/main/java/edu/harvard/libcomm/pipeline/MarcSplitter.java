package edu.harvard.libcomm.pipeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.List;
import java.util.ArrayList;

import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;

import edu.harvard.libcomm.message.LibCommMessage;


public class MarcSplitter  {

	public MarcFileIterator splitMarcFile(String body) throws FileNotFoundException {

		LibCommMessage libCommMessage = MessageUtils.unmarshalLibCommMessage(new StringReader(body));

		String filepath = libCommMessage.getPayload().getFilepath();
		InputStream input = null;
		input = new FileInputStream(filepath);			

	    MarcReader reader = new MarcStreamReader(input);
	    return new MarcFileIterator(reader);
	}

}