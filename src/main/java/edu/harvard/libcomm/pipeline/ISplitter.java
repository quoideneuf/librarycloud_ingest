package edu.harvard.libcomm.pipeline;

import java.io.InputStream;
import java.util.Iterator;

import edu.harvard.libcomm.message.LibCommMessage;

interface ISplitter {

	public Iterator getIterator(InputStream is) throws Exception;

}