package edu.harvard.libcomm.pipeline.delete;

import java.io.InputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import edu.harvard.libcomm.pipeline.ISplitter;

public class DeleteFileSplitter implements ISplitter {
	protected Logger log = Logger.getLogger(DeleteFileSplitter.class); 	

	public Iterator getIterator(InputStream is) throws Exception {
		DeleteFileIterator deleteFileIterator = new DeleteFileIterator();
	    return deleteFileIterator.getDeleteIds(is);
	}


}
