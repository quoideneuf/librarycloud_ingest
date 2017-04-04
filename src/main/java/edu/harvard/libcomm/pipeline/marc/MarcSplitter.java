package edu.harvard.libcomm.pipeline.marc;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;

import edu.harvard.libcomm.pipeline.ISplitter;

public class MarcSplitter implements ISplitter {
	protected Logger log = Logger.getLogger(MarcSplitter.class); 	
	private long chunkSize;

	public MarcSplitter(long chunkSize) {
		this.chunkSize = chunkSize;
	}

	public Iterator getIterator(InputStream is) {
	    MarcReader reader = new MarcStreamReader(is);
	    return new MarcFileIterator(reader, this.chunkSize);
	}

}