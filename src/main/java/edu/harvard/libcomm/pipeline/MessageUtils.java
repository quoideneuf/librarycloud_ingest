package edu.harvard.libcomm.pipeline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;

import org.apache.camel.Message;
import org.apache.camel.component.file.GenericFile;

import edu.harvard.libcomm.message.LibCommMessage;
import gov.loc.marc.CollectionType;
import gov.loc.mods.v3.ModsCollection;
import gov.loc.mods.v3.ModsType;

public class MessageUtils {

	private static LibCommMessage libCommMessage = null;
	private static String messageString = null;

    static final JAXBContext context = initContext();

    private static JAXBContext initContext()  {
    	JAXBContext context = null;
    	try {
    		context = JAXBContext.newInstance(LibCommMessage.class,ModsCollection.class,CollectionType.class);
    	} catch (JAXBException je) {
    		System.out.println(je);
    	}
    	return context;
    }	


    protected static JAXBSource getJAXBSource(Object o) {
    	JAXBSource source = null;
    	try {
    		source = new JAXBSource(context, o);
    	}
    	catch (JAXBException je) {
    		je.printStackTrace();
    	}
    	return source;
    }
    
	protected static LibCommMessage unmarshalLibCommMessage(Reader r) {
		
	 	try {			 
			//unmarshal: xml2java
	 		Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
			libCommMessage = (LibCommMessage) jaxbUnmarshaller.unmarshal(r);
		  } catch (JAXBException e) {
			e.printStackTrace();
		  }
		return libCommMessage;
		
	}
	

	protected static LibCommMessage unmarshalLibCommMessage(InputStream is) {
		
	 	try {
			//unmarshal: xml2java
	 		Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
			libCommMessage = (LibCommMessage) jaxbUnmarshaller.unmarshal(is);
		  } catch (JAXBException e) {
			e.printStackTrace();
		  }
		return libCommMessage;		
	}
	
	protected static ModsCollection unmarshalMods(Reader r) {
		ModsCollection modsCollection = null;
	 	try {
			 
			//unmarshal: xml2java
	 		Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
			modsCollection = (ModsCollection) jaxbUnmarshaller.unmarshal(r);
 
		  } catch (JAXBException e) {
			e.printStackTrace();
		  }
		return modsCollection;
		
	}

	protected static CollectionType unmarshalMarc(Reader r) {
		CollectionType collectionType = null;
	 	try {
			 
			//unmarshal: xml2java
	 		Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
	 		collectionType = (CollectionType) jaxbUnmarshaller.unmarshal(r);
 
		  } catch (JAXBException e) {
			e.printStackTrace();
		  }
		return collectionType;
		
	}
	
	protected static String marshalMessage(LibCommMessage libCommMessage) {
		StringWriter sw = null;
	 	try {
			 
			//marshal:java2xml
			//messageOS = new OutputStream();
			sw = new StringWriter();
			Marshaller jaxbMarshaller = context.createMarshaller();
			// output pretty printed
			//jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(libCommMessage, sw);
		
		  } catch (JAXBException e) {
			e.printStackTrace();
		  }
		return sw.toString();
	}
	
}
