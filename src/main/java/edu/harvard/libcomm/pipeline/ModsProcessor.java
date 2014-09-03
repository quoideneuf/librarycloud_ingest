package edu.harvard.libcomm.pipeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;
import gov.loc.mods.v3.ModsCollection;

public class ModsProcessor extends LibCommProcessor {

	public void process(Exchange exchange) throws Exception {
		
		Message message = exchange.getIn();
		InputStream messageIS = readMessageBody(message);	 
		
		StringReader reader = transformMarcToMods(messageIS); 
        
		modsCollection = unmarshalMessage(reader);
        
		modifyMessage(libCommMessage);
       
		String messageString = MessageUtils.marshalMessage(libCommMessage);
        
		message.setBody(messageString);
		exchange.setOut(message);
		
	}


	@Override
	public void modifyMessage(LibCommMessage libComMessage) {
        libCommMessage = new LibCommMessage();
        libCommMessage.setCommand("NORMALIZE");
        Payload payload = new Payload();
        payload.setSource("aleph");
        payload.setFormat("marcxml");

        payload.setAny(modsCollection);
        libCommMessage.setPayload(payload);
	}	

	private StringReader transformMarcToMods (InputStream messageIS) throws Exception {
		StringWriter writer = new StringWriter();
		final InputStream xsl = new FileInputStream("src/main/resources/MARC21slim2MODS3-5.xsl");

        final TransformerFactory tFactory = TransformerFactory.newInstance();

        StreamSource styleSource = new StreamSource(xsl);
        Transformer transformer = tFactory.newTransformer(styleSource);
        
        StreamSource xmlSource = new StreamSource(messageIS);
        StreamResult result = new StreamResult(writer);
        transformer.transform(xmlSource, result);
        StringReader reader = new StringReader(writer.toString());
        return reader;
	}
	
	public void test() {
		
	}
	
}
