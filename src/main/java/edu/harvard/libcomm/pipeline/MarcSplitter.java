package edu.harvard.libcomm.pipeline;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;


public class MarcSplitter implements ISplitter {
	protected Logger log = Logger.getLogger(MarcSplitter.class); 	

	public Iterator getIterator(InputStream is) {
	    MarcReader reader = new MarcStreamReader(is);
	    return new MarcFileIterator(reader);
	}

}