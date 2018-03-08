package edu.harvard.libcomm.pipeline.update;

import edu.harvard.libcomm.pipeline.ISplitter;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.Iterator;

public class UpdateFileSplitter implements ISplitter {
	protected Logger log = Logger.getLogger(UpdateFileSplitter.class);

	public Iterator getIterator(InputStream is) throws Exception {
		UpdateFileIterator updateFileIterator = new UpdateFileIterator();
	    return updateFileIterator.getUpdateUrns(is);
	}


}
