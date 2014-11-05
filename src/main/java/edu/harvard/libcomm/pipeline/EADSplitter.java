package edu.harvard.libcomm.pipeline;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;


public class EADSplitter implements ISplitter {
	protected Logger log = Logger.getLogger(EADSplitter.class); 	

	public Iterator getIterator(InputStream is) throws Exception {
		EADReader reader = new EADReader(is);
	    return new EADComponentIterator(reader);
	}


}