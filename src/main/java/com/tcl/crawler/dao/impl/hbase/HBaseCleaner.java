package com.tcl.crawler.dao.impl.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;

public final class HBaseCleaner {
	public static void clean(String tableName) throws IOException{
		HBaseAdmin admin = new HBaseAdmin(HBasePool.getInstance().getConf());
		admin.disableTable(tableName);
		admin.deleteTable(tableName);
		/*for(HTableDescriptor d : admin.listTables()){
			System.out.println(d.getNameAsString());
			for(HColumnDescriptor cd : d.getColumnFamilies()){
				System.out.println("===="+cd.getNameAsString());
			}
		}*/
		HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);  
        tableDescriptor.addFamily(new HColumnDescriptor("content"));  
        tableDescriptor.addFamily(new HColumnDescriptor("title"));  
		admin.createTable(tableDescriptor);
		admin.close();
	}
	
	public static void main(String[] args) throws IOException {
		clean("page");
	}
}
