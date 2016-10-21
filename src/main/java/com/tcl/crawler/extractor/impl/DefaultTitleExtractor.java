package com.tcl.crawler.extractor.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tcl.crawler.extractor.TitleExtractor;

public class DefaultTitleExtractor implements TitleExtractor {

	public String extractFromHtml(String html) {
		String re = "";
		if(html == null){
			return re ;
		}
		String regex = "<title>(.+?)</title>";
		Pattern pt = Pattern.compile(regex);
		Matcher mt = pt.matcher(html);
		if (mt.find()) {
			re = mt.group(1);
		}
		return re;
	}

}
