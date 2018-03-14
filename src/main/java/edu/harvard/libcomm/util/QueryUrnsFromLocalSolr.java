package edu.harvard.libcomm.util;

import edu.harvard.libcomm.pipeline.Config;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CursorMarkParams;

import java.io.FileWriter;

/**
 * Created by mjv162 on 10/19/2017.
 * Based on:
 * http://www.codecognition.org/2015/03/using-cursormark-for-deep-paging-in-solr.html
 * and Solr documentation examples
 */
public class QueryUrnsFromLocalSolr {
    private static final int MAX_ROWS=200;
    private static final String URL= "http://lc-solr1.lib.harvard.edu:8983/solr/librarycloud"; //Config.getInstance().SOLR_EXTENSIONS_URL;
    private static final String UNIQUE_ID_FIELD="id";
    //private static final String SOLR_QUERY="NOT targetField:[* TO *]";
    private static String type = null;
    private static String field = "urn";
    private static String filepath = null;

    public static void main (String[] args) {

        if (args.length != 1) {
            System.out.println("java QueryUrnsFromSolr <filepath>");
        }
        //type = args[0];
        filepath = args[0];

        try {
            QueryUrnsFromLocalSolr qry = new QueryUrnsFromLocalSolr();
            qry.queryForUrns();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void queryForUrns() throws Exception {
        SolrServer server = new HttpSolrServer(URL);
        //String queryStr = "inDRS:true AND accessFlag:null";
        String queryStr = "source:\"MH:VIA\" AND inDRS:false AND urn:[* TO *]";
        System.out.println(queryStr);
        SolrQuery q = (new SolrQuery(queryStr)).setFields(field).setRows(MAX_ROWS).setSort(SolrQuery.SortClause.asc("recordIdentifier"));
        q.add("wt","json");
        String cursorMark = CursorMarkParams.CURSOR_MARK_START;
        boolean done = false;
        FileWriter file = new FileWriter(filepath + "/via_indrs_false.txt");
        String newLine = System.getProperty("line.separator");
        while (! done) {
            q.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);
            QueryResponse rsp = server.query(q);
            String nextCursorMark = rsp.getNextCursorMark();
            String urns = writeUrns(rsp);
            //System.out.println(urns);
            file.write(urns + newLine);
            if (cursorMark.equals(nextCursorMark)) {
                done = true;
            }
            cursorMark = nextCursorMark;
        }
        file.close();
        System.out.println("DONE");
    }

    private String writeUrns(QueryResponse rsp) {
        SolrDocumentList docs = rsp.getResults();
        StringBuilder sb = new StringBuilder();
        for(SolrDocument doc : docs) {
            //System.out.println(doc);
            sb.append("," + doc.getFirstValue(field).toString());
        }
        return sb.toString().replaceFirst(",","").replace(" ","");
    }

}
