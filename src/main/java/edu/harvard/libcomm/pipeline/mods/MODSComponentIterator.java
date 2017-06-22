package edu.harvard.libcomm.pipeline.mods;

import edu.harvard.libcomm.pipeline.Config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.net.URI;

import javax.xml.bind.JAXBException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONTokener;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;
import edu.harvard.libcomm.pipeline.MessageUtils;

public class MODSComponentIterator implements Iterator<String> {
	protected Logger log = Logger.getLogger(MODSComponentIterator.class);

	protected MODSReader modsReader;
	protected NodeList nodes;
	protected DOMSource domSource;
	protected Transformer transformer;
	protected int position = 0;

    public MODSComponentIterator(MODSReader reader) throws Exception {
        this.modsReader = reader;
        nodes = reader.getNodes();
        domSource = reader.getDOMSource();
        transformer = buildTransformer("src/main/resources/mods2modscomponent.xsl");
    }

    @Override
    public boolean hasNext() {
    	return ((nodes != null) && (position < nodes.getLength()));
    }

    @Override
    public String next() {
    	//log.trace("Processing node " + position + " of " + nodes.getLength());
        String modsComponentMods = "";
    	while ((nodes != null) && (position < nodes.getLength())) {
	        String nodeName = nodes.item(position).getNodeName();
	        String nodeValue = nodes.item(position).getNodeValue();
            String nodeValueChopped = nodeValue.substring(nodeValue.indexOf("urn-3"), nodeValue.length()).split("\\?")[0];

            position++;

            JSONTokener tokener = null;
            try {
                URI uri = new URI(Config.getInstance().DRSEXTENSIONS_URL + "?urns=" + nodeValueChopped);
                tokener = new JSONTokener(uri.toURL().openStream());
                System.out.println(uri.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject json = new JSONObject(tokener);
            JSONArray jsonArr = json.getJSONArray("extensions");
            boolean inDRS = jsonArr.getJSONObject(0).getBoolean("inDRS");

            String fileDeliveryUrl = null;
            if (inDRS) {
                //fileDeliveryUrl = jsonArr.getJSONObject(0).getString("fileDeliveryURL");
                //System.out.println("fileDeliveryUrl: " + fileDeliveryUrl);
                try {
                    //if (fileDeliveryUrl != null)
                        modsComponentMods += transformMODS(nodeValue);
                    //System.out.println("modsComponentMods: " + modsComponentMods);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new NoSuchElementException();
                }
                break;
            }
    	}

        /*LibCommMessage lcmessage = new LibCommMessage();
        Payload payload = new Payload();
        payload.setFormat("MODS");
        payload.setSource("ALEPH");
        payload.setData(modsComponentMods);
        lcmessage.setCommand("ENRICH");
        lcmessage.setPayload(payload);
        try {
            return MessageUtils.marshalMessage(lcmessage);
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }*/

    	return modsComponentMods;
	}

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    protected Transformer buildTransformer(String xslFilePath) throws Exception {
		final InputStream xsl = new FileInputStream(xslFilePath);
		final TransformerFactory tFactory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl",null);
        StreamSource styleSource = new StreamSource(xsl);
        return tFactory.newTransformer(styleSource);    	
    }

	protected String transformMODS (String xslParam) throws Exception {		
       	this.transformer.setParameter("url", xslParam);
		StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(this.domSource, result);
        return writer.toString();
	}
    
}