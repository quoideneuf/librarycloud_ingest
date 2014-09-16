package edu.harvard.libcomm.pipeline;

import java.io.ByteArrayOutputStream;
import java.lang.UnsupportedOperationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.MarcWriter;
import org.marc4j.MarcXmlWriter;
import org.marc4j.marc.Record;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;

public class MarcFileIterator implements Iterator<String> {

    private MarcReader marcReader;
    private int chunkSize = 25;

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
        while (marcReader.hasNext() && newChunk == false) {
       // while (marcReader.hasNext() && firstRecord) {
            try {
                Record record = marcReader.next();
                writer.setIndent(false);
                writer.write(record);
                count++;           
                totalSize += output.toString().length();
                newChunk = totalSize > 150000 ? true:false;
                totalSize = 0;
            } catch (org.marc4j.MarcException ex) {
                ex.printStackTrace();
            }
        }   
        if (count > 0) {
        	writer.close();
        	//System.out.println(totalSize);
        	System.out.println(count);
        	//return output.toString();
             LibCommMessage message = new LibCommMessage();
             message.setCommand("NORMALIZE");
             Payload payload = new Payload();
             payload.setSource("aleph");
             payload.setFormat("mods");
             payload.setData(output.toString());
             message.setPayload(payload);
             return MessageUtils.marshalMessage(message);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
