package com.tcl.crawler.extractor;

import java.util.Set;

import com.tcl.crawler.utils.URL;

/**
 * @packageName com.tcl.crawler.extractor
 */
public interface LinkExtractor {
	public Set<URL> extractFromHtml(String html, final int level);
}