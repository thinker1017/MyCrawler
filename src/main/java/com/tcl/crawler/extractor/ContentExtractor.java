package com.tcl.crawler.extractor;


/**
 * Extract the main text of an url
 * @packageName com.gs.extractor
 */
public interface ContentExtractor {
	public String extractFromHtml(String html);
	
}