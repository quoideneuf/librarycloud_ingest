package edu.harvard.libcomm.pipeline;

import java.net.URL;
import java.net.MalformedURLException;
import org.apache.camel.Exchange;  
import org.apache.camel.Message;  

/**
 * This bean takes as input a message with a URL in the body, and updates
 * the message body and headers so that it will work when passed to the 
 * Camel HTTP component for downloading a file. 
 */
public class PrepareURIForHTTPEndpoint {  

 	public void prepare(String body, Exchange exchange) throws MalformedURLException {
 		
 		/* Set the header for the URI which we want to access */
 		exchange.getIn().setHeader(Exchange.HTTP_URI, body);
 		
 		/* Use a header to override the query parameters. If we do not
 		   do this, and just rely on the parameters being passed as part
 		   of the URI, they get URI encoded improperly */
 		URL url = new URL(body);
 		exchange.getIn().setHeader(Exchange.HTTP_QUERY, url.getQuery().replace("&amp;","&")); 		

 		/* Set the body to be empty, since we're not POSTing data to the URI */
 		exchange.getIn().setBody("");

 		/* Make sure that we're using GET - it's not getting set automatically */
 		exchange.getIn().setHeader(Exchange.HTTP_METHOD, "GET");
 	}
}