package com.tcl.crawler.dao.impl.hbase;

import java.io.IOException;
import java.util.Set;

import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcl.crawler.dao.PageDAO;
import com.tcl.crawler.model.PagePOJO;

/**
 * PageDAO的HBase实现版本
 * 
 */
public class PageDAOHBaseImpl implements PageDAO {
	private HTableInterface table;
	private static final Logger LOG = LoggerFactory
			.getLogger(PageDAOHBaseImpl.class);

	public PageDAOHBaseImpl(String tableName)
			throws ZooKeeperConnectionException, IOException {
		table = HBasePool.getInstance().getTable(tableName);
	}

	public PagePOJO loadPage(String url) {
		PagePOJO pojo = new PagePOJO();
		try {
			Get scan = new Get(url.getBytes());// 设置ID号
			org.apache.hadoop.hbase.client.Result r = table.get(scan);
			for (org.apache.hadoop.hbase.KeyValue keyValue : r.raw()) {
				if (new String(keyValue.getFamily()).equals("content")) {
					pojo.setContent(new String(keyValue.getValue()));
				} else if (new String(keyValue.getFamily()).equals("title")) {
					pojo.setTitle(new String(keyValue.getValue()));
				}
			}
			pojo.setUrl(url);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				table.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pojo;
	}

	public boolean exist(String url) {
		// TODO Auto-generated method stub
		return false;
	}

	public void delete(String url) {
		// TODO Auto-generated method stub

	}

	public void save(PagePOJO pojo) throws IOException {
		Put put = new Put(pojo.getUrl().getBytes());
		put.add("content".getBytes(), null, pojo.getContent().getBytes());
		put.add("title".getBytes(), null, pojo.getTitle().getBytes());
		try {
			table.put(put);
			table.close();
		} catch (IOException e) {
			LOG.error("存入数据时发生错误 数据："+pojo.toString()+" 错误信息： "+e.getMessage());
			throw e;
		}
	}

	public void save(Set<PagePOJO> set) throws IOException {
		for (PagePOJO pojo : set) {
			Put put = new Put(pojo.getUrl().getBytes());
			put.add("content".getBytes(), null, pojo.getContent().getBytes());
			put.add("title".getBytes(), null, pojo.getTitle().getBytes());
			try {
				table.put(put);
			} catch (IOException e) {
				LOG.error("存入数据时发生错误 数据："+pojo.toString()+" 错误信息： "+e.getMessage());
				throw e;
			}
		}
		table.close();
	}

	public void close() throws IOException {
		try {
			table.close();
		} catch (Exception e) {
			LOG.error("关闭Table时发生错误"+e.getMessage());
		}
	}

}
