package com.tcl.crawler.extractor.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tcl.crawler.extractor.ContentExtractor;

/**
 * http://3g.163.com/news
 *
 */
public class WangYiWapNewsContentExtractor implements ContentExtractor {

	public String extractFromHtml(String html) {
		String regex = "<div class=\"content\">(.*?)</div>";
		Pattern pt = Pattern.compile(regex,Pattern.DOTALL);
		Matcher mt = pt.matcher(html);
		String re = "";
		while (mt.find()) {
			re += mt.group(0);
		}
		re = re.replaceAll("<.*?>", "");//抹掉所有尖括号的内容
		re = re.replaceAll("\\s", "");//抹掉所有空白
		return re;
	}

}
