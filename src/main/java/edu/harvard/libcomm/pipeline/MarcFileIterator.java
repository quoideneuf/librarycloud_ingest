package edu.harvard.libcomm.pipeline;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.UnsupportedOperationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.bind.JAXBException;

import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.Record;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;

public class MarcFileIterator implements Iterator<String> {

    private MarcReader marcReader;

    public MarcFileIterator(MarcReader marcReader) {
        this.marcReader = marcReader;
    }

    @Override
    public boolean hasNext() {                
        return marcReader.hasNext();
    }

    @Override
    public String next() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        MarcXmlWriter writer = new MarcXmlWriter(output, true);
        int count = 0;
        boolean newChunk = false;
        int totalSize = 0;
        if (marcReader.hasNext()) {
            try {
                Record record = marcReader.next();
                // writer.setIndent(false);
                writer.write(record);
                writer.close();
            } catch (org.marc4j.MarcException ex) {
                ex.printStackTrace();
                throw new NoSuchElementException();
            }
            LibCommMessage message = new LibCommMessage();
            message.setCommand("NORMALIZE");
            Payload payload = new Payload();
            payload.setSource("aleph");
            payload.setFormat("mods");
            try {
				payload.setData(output.toString("UTF-8").replace("<collection>", "<collection " + "xmlns=\"http://www.loc.gov/MARC21/slim\"" + ">"));
            } catch (UnsupportedEncodingException e) {
			    e.printStackTrace();
                throw new NoSuchElementException();
			}
            message.setPayload(payload);
            try {
				return MessageUtils.marshalMessage(message);
			} catch (JAXBException e) {
				e.printStackTrace();
                throw new NoSuchElementException();
			}
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
