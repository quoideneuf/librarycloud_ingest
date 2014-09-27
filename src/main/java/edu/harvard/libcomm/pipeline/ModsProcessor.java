package edu.harvard.libcomm.pipeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.util.JAXBSource;
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
import gov.loc.marc.CollectionType;
import gov.loc.mods.v3.ModsCollection;

public class ModsProcessor implements IProcessor {

	public void processMessage(LibCommMessage libCommMessage) throws Exception {	
		String modsCollection = null;
		libCommMessage.setCommand("ENHANCE");
		try {
			modsCollection = transformMarcToMods(libCommMessage);	
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
        Payload payload = new Payload();
        payload.setSource("aleph");
        payload.setFormat("mods");
        payload.setData(modsCollection);
        libCommMessage.setPayload(payload);
	}

	private String transformMarcToMods (LibCommMessage libCommMessage) throws Exception {
		String data = libCommMessage.getPayload().getData();
		//System.out.println("DATA: " + data);
		StringReader marcReader = new StringReader(data);
		
		StringWriter writer = new StringWriter();
		final InputStream xsl = new FileInputStream("src/main/resources/MARC21slim2MODS3-5.xsl");

        final TransformerFactory tFactory = TransformerFactory.newInstance();

        StreamSource styleSource = new StreamSource(xsl);
        Transformer transformer = tFactory.newTransformer(styleSource);
        
        StreamSource xmlSource = new StreamSource(marcReader);
        StreamResult result = new StreamResult(writer);

        transformer.transform(xmlSource, result);
        StringReader reader = new StringReader(writer.toString());
        return writer.toString();
	}

	
	
}
