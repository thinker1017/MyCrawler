package com.tcl.crawler.indexer.solr;

import java.io.IOException;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcl.crawler.model.PagePOJO;

public class SolrIndex {
	private static final Logger LOG = LoggerFactory.getLogger(SolrIndex.class);
	
	/**
	 * @param set 待索引的PagePOJO集合
	 * @param SERVER_URL Solr服务器的ServerURL,例如:http://localhost:8983/solr/
	 * @throws SolrServerException
	 * @throws IOException
	 */
	public static final void index(final Set<PagePOJO> set,
			final String SERVER_URL) throws SolrServerException, IOException {
		long startTime = System.currentTimeMillis();
		LOG.info("Index Start .Solr server "+SERVER_URL+" . Index number : "+set.size());
		SolrServer server = new HttpSolrServer(SERVER_URL);
		//Set<SolrInputDocument> result = new HashSet<SolrInputDocument>();
		for (PagePOJO pojo : set) {
			SolrInputDocument doc;
			doc = new SolrInputDocument();
			doc.addField("title", pojo.getTitle());
			doc.addField("content", pojo.getContent());
			doc.addField("url", pojo.getUrl());
			//result.add(doc);
			server.add(doc);
		}
		//server.add(result);
		server.commit();
		LOG.info("Index Finished! Use Time :  "+(System.currentTimeMillis() - startTime)+" s");
	}
}
