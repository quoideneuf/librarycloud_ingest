package edu.harvard.libcomm.pipeline;

import java.io.InputStream;
import java.util.Iterator;

interface ISplitter {

	public Iterator getIterator(InputStream is) throws Exception;

}