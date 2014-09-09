package edu.harvard.libcomm.pipeline;

import edu.harvard.libcomm.message.LibCommMessage;

interface IProcessor {

	public void processMessage(LibCommMessage message);

}