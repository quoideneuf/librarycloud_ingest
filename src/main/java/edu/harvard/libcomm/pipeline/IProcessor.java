package edu.harvard.libcomm.pipeline;

import edu.harvard.libcomm.message.LibCommMessage;

public interface IProcessor {

	public void processMessage(LibCommMessage message) throws Exception;

}