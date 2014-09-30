package edu.harvard.libcomm.pipeline;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import edu.harvard.libcomm.message.LibCommMessage;
import gov.loc.mods.v3.ModsCollection;
import gov.loc.mods.v3.ModsType;

public class HoldingsProcessor implements IProcessor {
	
	public void processMessage(LibCommMessage libCommMessage) {	
	
		String data = null;
		String recids = null;
		libCommMessage.setCommand("PUBLISH");
		try {
			recids = MessageUtils.transformPayloadData(libCommMessage,"src/main/resources/recids.xsl");
		} catch (Exception e) {
			e.printStackTrace();
		}

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("http://hlslwebtest.law.harvard.edu/v2/api/holdings/?filter=Marc001:(" + recids + ")");
        try {
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        while ((line = rd.readLine()) != null) {
          System.out.println(line);
        }
        } catch (Exception e) {
        	e.printStackTrace();
        }
		
		/*
		StringReader modsReader = new StringReader(data);
		ModsCollection modsCollection = MessageUtils.unmarshalMods(modsReader);
		List<ModsType> modsList =  modsCollection.getMods();
        Iterator<ModsType> modsIterator = modsList.iterator();
        while (modsIterator.hasNext()) {
           List<Object> modsGroup = modsIterator.next().getModsGroup();
           System.out.println("MODSGRP1: " + modsGroup.get(0).toString());
        }
		*/
	}
}
