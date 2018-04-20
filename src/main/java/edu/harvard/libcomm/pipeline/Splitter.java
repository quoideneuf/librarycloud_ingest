package edu.harvard.libcomm.pipeline;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Iterator;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;

import org.apache.commons.validator.routines.UrlValidator;
import org.apache.log4j.Logger;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;

import edu.harvard.libcomm.message.LibCommMessage;

/**
 * Takes as a parameter to split() a command message with the URL or system path of a file to be ingested,
 * and returns an iterator (set using setSplitter()) that iterates over the contents of
 * the referenced file.
 */
public class Splitter  {
    protected Logger log = Logger.getLogger(Splitter.class);
    private ISplitter splitter;

    @Autowired
    private AmazonS3 s3Client;


    public Iterator split(String body) throws Exception {
        return splitter.getIterator(getResourceInputStream(body));
    }

    protected InputStream getResourceInputStream(String body) throws FileNotFoundException, IOException, JAXBException  {
        log.debug(body);
        LibCommMessage libCommMessage = MessageUtils.unmarshalLibCommMessage(new StringReader(body));

        if ((libCommMessage != null) && (libCommMessage.getPayload() != null)) {
            String resource = libCommMessage.getPayload().getFilepath();
            log.debug("FILEPATH ------- " + resource);
            InputStream input = null;

            /* Figure out if the path is a URI or a local file system reference, and handle appropriately */
            // UrlValidator urlValidator = new UrlValidator();
            // if (urlValidator.isValid(resource)) {
            //     try {
            //         input = new URL(resource).openStream();
            //     } catch (IOException ex) {
            //         ex.printStackTrace();
            //         throw ex;
            //     }
            if (resource.startsWith("aws-s3://")) {
                String filepath = libCommMessage.getPayload().getFilepath().replace("aws-s3://", "");
                String[] parts = filepath.split("/");
                String bucket = parts[0];
                String key = parts[1];
                S3Object getResult = null;

                try {
                    getResult = s3Client.getObject(bucket, key);
                    input = getResult.getObjectContent();
                } catch (Exception e) {
                    log.error(e);
                    e.printStackTrace();
                } finally {
                    if (getResult != null) {
                        // getResult.close();
                    }
                }
            } else {
                // If it's not a URL, assume it's a file path
                try {
                    input = new FileInputStream(resource);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                    throw ex;
                }
            }
            return input;
        } else {
            System.out.println("LOGGING TO DO - Something bad happenned in parsing incoming message");
            throw new RuntimeException("Error parsing payload");
        }
    }

    public ISplitter getSplitter() {
        return this.splitter;
    }

    public void setSplitter(ISplitter splitter) {
        this.splitter = splitter;
    }

}
