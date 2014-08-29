package edu.harvard.libcomm.pipeline;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import edu.harvard.libcomm.message.LibCommMessage;

public interface LibCommProcessor extends Processor {

	public InputStream readMessageBody (Message message) ;
	
	public LibCommMessage unmarshalMessage (InputStream messageIS);

	public String marshallMessage (LibCommMessage libCommMessage);
	
	public void modifyMessage(LibCommMessage libComMessage);
	
	
}
