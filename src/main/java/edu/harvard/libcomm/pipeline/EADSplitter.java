package edu.harvard.libcomm.pipeline;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;


public class EADSplitter implements ISplitter {
	protected Logger log = Logger.getLogger(EADSplitter.class); 	

	public Iterator getIterator(InputStream is) throws Exception {
		String xslFilePath = Config.getInstance().EAD_COMPONENT_XSL;
		String componentXpath = "//c/@id";
		XMLReader reader = new XMLReader(is , xslFilePath , componentXpath);
	    return new EADComponentIterator(reader);
	}


}