package edu.harvard.libcomm.pipeline.mods;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import edu.harvard.libcomm.pipeline.ISplitter;

public class MODSRawSplitter implements ISplitter {
	protected Logger log = Logger.getLogger(MODSRawSplitter.class); 	

	public Iterator getIterator(InputStream is) throws Exception {
		
		MODSReader reader = new MODSReader(is);
	    return new MODSRawComponentIterator(reader);
	}


}