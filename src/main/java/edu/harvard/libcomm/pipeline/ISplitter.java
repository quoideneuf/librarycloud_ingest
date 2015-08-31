package edu.harvard.libcomm.pipeline;

import java.io.InputStream;
import java.util.Iterator;

public interface ISplitter {

	public Iterator getIterator(InputStream is) throws Exception;

}