package edu.harvard.libcomm.pipeline.enrich;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.*;

import edu.harvard.libcomm.message.LibCommMessage;
import edu.harvard.libcomm.message.LibCommMessage.Payload;
import edu.harvard.libcomm.pipeline.Config;
import edu.harvard.libcomm.pipeline.IProcessor;
import edu.harvard.libcomm.pipeline.MessageUtils;

/* Add Holdings data (physicalLocation, shelfLocator, url) to MODS records, retrieved from lilCloud API */
public class DRSExtensionsProcessor extends ExternalServiceProcessor implements IProcessor {

	protected Logger log = Logger.getLogger(DRSExtensionsProcessor.class);

	public void processMessage(LibCommMessage libCommMessage) throws Exception {	
	
		URI uri = null;
		String urns = getUrns(libCommMessage);
		//urns = urns.startsWith("OR") ? urns.substring(2) : urns;
		//can't throw out all urns bc one has ebook, only looking for recs with no urns at all
		//if (urns.equals("")  || urns == null || urns.contains("ebook"))
		//System.out.println("%%%%%%%%%%% URNS: " + urns);
		//if (urns.equals("")  || urns == null)
		if (!urns.contains("urn-3"))
			uri = null;
		else {
		urns = urns.endsWith(" OR ") ? urns.substring(0, urns.length() - 4) : urns;
			//Why are we getting this condition? TO DO - catch upstream
			urns = urns.replace("OR  OR ", "OR ");
			urns = "(" + urns.replace(" ","+") + ")";
			//System.out.println("URNS: " + urns);
			uri = new URI(Config.getInstance().SOLR_EXTENSIONS_URL + "/select?q=urn_keyword:" + urns + "&rows=250");
		}
		//System.out.println("URI: " + uri.toString());
		//processWithThumbCheck(libCommMessage, uri, "results", "src/main/resources/adddrsextensions.xsl");
		process(libCommMessage, uri, "results", "src/main/resources/adddrsextensions.xsl");
	}

	//need to refactor to own enrichment step
    protected void processWithThumbCheck(LibCommMessage libCommMessage, URI uri, String wrapperToken, String transformXSL) throws Exception {

        String data = null;
        String xml = "";
        if (uri == null) {
            xml = "";
        }
        else {
            JSONTokener tokener;
            try {
                //tokener = new JSONTokener(uri.toURL().openStream());
                tokener = new JSONTokener(IOUtils.toString(uri.toURL().openStream(), "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }

            JSONObject json = new JSONObject(tokener);
            xml = XML.toString(json);
        }
        xml = "<" + wrapperToken + ">" + xml + "</" + wrapperToken + ">";
        //System.out.println("EXT XML: " + xml);
        log.trace("External Service result:" + xml);

        try {
            data = MessageUtils.transformPayloadData(libCommMessage,transformXSL,xml);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        //System.out.println("DATA: " + data);
		String urls = MessageUtils.transformPayloadData(data,"src/main/resources/pds_urns.xsl", null);
		String results = "";
		if (urls.length() > 0) {
			urls = urls.replace(",*$", "");
			//String urls = "http://nrs.harvard.edu/urn-3:fhcl.loeb:957096,http://nrs.harvard.edu/urn-3:HUL.ARCH:26515075?n=13,http://nrs.harvard.edu/urn-3:fhcl.loeb:957096  ,http://nrs.harvard.edu/urn-3:HUL.FIG:006755588";
			StringBuilder sb = new StringBuilder();
			for (String s : urls.split(",")) {
				if (s.contains("urn-3") && !s.contains("HUL.FIG") && !s.contains("ebookbatch") && !s.contains("ejournals")) {
					HttpURLConnection con = null;
					int responseCode = 0;
					try {
						con = (HttpURLConnection) (new URL(s).openConnection());
						con.setInstanceFollowRedirects(false);
						con.connect();
						responseCode = con.getResponseCode();
					} catch (IOException e) {
						e.printStackTrace();
					}
					String location = con.getHeaderField("Location"); // testing + "?n=3";
					if ((responseCode / 100) == 3) {
						if (location.contains("//pds")) {
							String thumb = getIIIFThumb(StringUtils.substringAfterLast(location, "/"));
							//System.out.println(location + " : " + thumb);
							sb.append("<image><url>" + s + "</url>" + "<thumb>" + thumb + "</thumb></image>");
						}
					}
				}
			}
			results = "<results>" + sb.toString() + "</results>";
			//System.out.println("results: " + results);
			String xmlWithThumbs = MessageUtils.transformPayloadData(data,"src/main/resources/addpdsurns.xsl",results);
			libCommMessage.getPayload().setData(xmlWithThumbs);
		}
		else {
			libCommMessage.getPayload().setData(data);
		}

	}

	private static String getIIIFThumb(String objectId) {
		String thumb = "";
		String iiifBaseUrl = "https://iiif.lib.harvard.edu/manifests/drs:";
		try {
			String id = objectId.split("\\?")[0];
			int seq = 1;
			if (objectId.contains("?n="))
				seq = Integer.parseInt(StringUtils.substringAfter(objectId,"?n="));
			URL iiifUrl = new URL(iiifBaseUrl + id); //(objectId.contains("?n=") ? objectId.replace("?n=","$") + "i" : objectId));
			InputStream is = iiifUrl.openConnection().getInputStream();
			try {
				JSONObject iiifObj = new JSONObject(readUrl(is));
				JSONArray seqArr = iiifObj.getJSONArray("sequences");
				JSONArray canvArr = seqArr.getJSONObject(0).getJSONArray("canvases");
				thumb = canvArr.getJSONObject(seq -1).getJSONObject("thumbnail").getString("@id");
			} catch (JSONException jse) {
				jse.printStackTrace();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return thumb;
	}

	private static String readUrl(InputStream is) {
		StringBuilder content = new StringBuilder();
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
			String line;
			// read from the urlconnection via the bufferedreader
			while ((line = bufferedReader.readLine()) != null) {
				content.append(line + "\n");
			}
			bufferedReader.close();
		} catch (Exception e) {
			System.out.println("BAD REQ: " + e.getMessage());
			e.printStackTrace();
		}
		//System.out.println("READURL: " + content.toString());
		return content.toString();
	}
}
