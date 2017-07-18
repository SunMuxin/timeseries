package com.realsight.westworld.tsp.lib.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.*;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class SolrDelete{
	private final String SOLR_URL;
	private final HttpSolrClient SOLR_Client;
	
	private static Logger logger = (Logger) LoggerFactory.getLogger(SolrDelete.class);
	
	public String getSOLR_URL() {
		return SOLR_URL;
	}
	
	public SolrDelete(String SOLR_URL) {
		this.SOLR_URL = SOLR_URL;
		this.SOLR_Client = new HttpSolrClient.Builder(this.SOLR_URL).build();
		this.SOLR_Client.setParser(new XMLResponseParser());
		this.SOLR_Client.setConnectionTimeout(1000 * 60 * 10);
	}
	
	public void delete_all() {
		SolrQuery params = new SolrQuery();
		params.setQuery("*:*");
		try {
			this.SOLR_Client.deleteByQuery(params.getQuery());
			logger.info("delete all");
		} catch (SolrServerException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
	
	public void close() throws IOException {
		this.SOLR_Client.close();
	}
}
