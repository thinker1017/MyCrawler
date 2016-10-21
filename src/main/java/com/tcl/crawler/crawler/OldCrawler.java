package com.tcl.crawler.crawler;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tcl.crawler.extractor.ContentExtractor;
import com.tcl.crawler.extractor.LinkExtractor;
import com.tcl.crawler.extractor.TitleExtractor;
import com.tcl.crawler.extractor.impl.DefaultContentExtractor;
import com.tcl.crawler.extractor.impl.DefaultLinkExtractor;
import com.tcl.crawler.extractor.impl.DefaultTitleExtractor;
import com.tcl.crawler.extractor.impl.HTMLDownloader;
import com.tcl.crawler.extractor.impl.SinaNewsContentExtractor;
import com.tcl.crawler.extractor.impl.SinaNewsLinkExtractor;
import com.tcl.crawler.extractor.impl.SinaNewsTitleExtractor;
import com.tcl.crawler.extractor.impl.TecentNewsTitleExtractor;
import com.tcl.crawler.extractor.impl.TencentNewsContentExtractor;
import com.tcl.crawler.extractor.impl.TencentNewsLinkExtractor;
import com.tcl.crawler.extractor.impl.WangYiWapNewsContentExtractor;
import com.tcl.crawler.extractor.impl.WangYiWapNewsLinkExtractor;
import com.tcl.crawler.extractor.impl.WangYiWapNewsTitleExtractor;
import com.tcl.crawler.model.PagePOJO;
import com.tcl.crawler.utils.URL;


@Deprecated
public class OldCrawler {
	private int deepth;
	/**
	 * 此Crawler对应ID，也就是qq文件中对应连接的偏移量
	 */
	private String id;

	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 抓取队列
	 */
	public LinkedList<URL> queue = new LinkedList<URL>();

	private int topN;
	private ContentExtractor contentExtractor;
	private LinkExtractor linkExtractor;
	private TitleExtractor titleExtractor;
	private String seed;

	/**
	 * @param id
	 * @param newsCenter
	 * @param topN
	 * @param deepth
	 */
	public OldCrawler(String id, String seed,
			int topN, int deepth) {
		this.id = id;
		this.topN = topN;
		this.deepth = deepth;
		this.seed = seed;
		switch (NewsCenterMatcher.match(seed)) {
		case Tencent:
			this.contentExtractor = new TencentNewsContentExtractor();
			this.linkExtractor = new TencentNewsLinkExtractor(deepth - 1, topN);
			this.titleExtractor = new TecentNewsTitleExtractor();
			break;
		case Sina:
			this.contentExtractor = new SinaNewsContentExtractor();
			this.linkExtractor = new SinaNewsLinkExtractor(deepth - 1, topN);
			this.titleExtractor = new SinaNewsTitleExtractor();
			break;
		case WangYiWap:
			this.contentExtractor = new WangYiWapNewsContentExtractor();
			this.linkExtractor = new WangYiWapNewsLinkExtractor(deepth - 1, topN);
			this.titleExtractor = new WangYiWapNewsTitleExtractor();
			break;
		case Unknow:
			this.contentExtractor = new DefaultContentExtractor();
			this.linkExtractor = new DefaultLinkExtractor(deepth - 1, topN);
			this.titleExtractor = new DefaultTitleExtractor();
			break;
		default:
			break;
		}
	}

	/**
	 * @param u
	 *            添加URL到此Crawler的抓取队列中
	 */
	public synchronized void addURL(URL u) {
		queue.add(u);
	}

	/**
	 * @param seeds
	 *            需要抓取的连接
	 * @return 返回抓取的储存json的LinkedList
	 */
	public List<String> start() {
		LinkedList<String> resultList = new LinkedList<String>();// 储存结果的List
		queue.add(new URL(seed, 1));// 将种子装入带抓取的队列
		int id = 0;// 每个页面的子ID号.
		Redis r;
		while (!queue.isEmpty()) {
			r = Redis.getInstance();// Redis为单例模式
			URL u = queue.remove();// 从队列里面拿出一个URL
			if (r.hasFetched(u)) {
				continue;
			}
			r.add(u.url,u.level);// 向Redis里添加URL
			String html = HTMLDownloader.down(u);// 下载html
			if (html == null || html.equals(""))// 判断html是否为空
				continue;
			for (URL url : linkExtractor.extractFromHtml(html, u.level)) {// 提取本页面的连接
				if (r.hasFetched(url))// 通过Redis判断是否已经抓取
					continue;// 若已经抓取则不再向待抓取队列中添加
				addURL(url);// 添加到Crawler的队列中
			}
			String content = contentExtractor.extractFromHtml(html);// 抽取本页面的内容
			if (content == null || content.equals(""))// 判断内容是否为空,即是否为新闻内容页面
				continue;
			PagePOJO pojo = new PagePOJO();
			pojo.setId(Integer.parseInt(this.id + id++));// 设置pageid
			pojo.setUrl(u.url);
			pojo.setContent(content);
			pojo.setTitle(titleExtractor.extractFromHtml(html));
			resultList.add(pojo.toJson());// 添加到结果List中
			pojo = null;// 置空pojo
			logger.info("Queue Size : " + queue.size() + "Level : " + u.level
					+ "URL : " + u.url);// 打印当前Queue状态
		}
		return resultList;
	}
}
