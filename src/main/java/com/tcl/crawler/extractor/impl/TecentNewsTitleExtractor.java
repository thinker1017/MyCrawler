package com.tcl.crawler.extractor.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tcl.crawler.extractor.TitleExtractor;

public class TecentNewsTitleExtractor implements TitleExtractor{
	public String extractFromHtml(String html){
		String re = "";
		if(html == null){
			return re ;
		}
		String regex = "<title>(.+?)_新闻_腾讯网</title>";//去掉最后的尾坠
		Pattern pt = Pattern.compile(regex);
		Matcher mt = pt.matcher(html);
		if (mt.find()) {
			re = mt.group(1);
		}
		return re;
	}
}
