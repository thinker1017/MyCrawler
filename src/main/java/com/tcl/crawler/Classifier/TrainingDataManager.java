package com.tcl.crawler.Classifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;


public class TrainingDataManager 
{
	private String[] traningFileClassifications;
	private File traningTextDir;
	private static String defaultPath = "D:\\Lucene\\Corpus\\";
	private Map<String,Map<String,Double>> classMap = new HashMap<String,Map<String,Double>>();
	private static TrainingDataManager ini = new TrainingDataManager();
	@SuppressWarnings("unchecked")
	private TrainingDataManager() 
	{
		traningTextDir = new File(defaultPath);
		if (!traningTextDir.isDirectory()) 
		{
			throw new IllegalArgumentException("ѵ�����Ͽ�����ʧ�ܣ� [" +defaultPath + "]");
		}
		this.traningFileClassifications = traningTextDir.list();
		String ss[] = traningTextDir.list();
		for(int i= 0;i<ss.length;i++){
			Map<String, Double> map = new HashMap<String, Double>();
			ObjectInputStream ois = null;
			try {
				FileInputStream is = new FileInputStream(defaultPath+ss[i]+"\\map");
				ois = new ObjectInputStream(is);
				map = (Map<String, Double>) ois.readObject();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			classMap.put(ss[i], map);
			map = null;
		}
	}
	
	public static TrainingDataManager getInstance(){
		return ini;
	}
	public String[] getTraningClassifications() 
	{
		return this.traningFileClassifications;
	}
	public String[] getFilesPath(String classification) 
	{
		File classDir = new File(traningTextDir.getPath() +File.separator +classification);
		String[] ret = classDir.list();
		for (int i = 0; i < ret.length; i++) 
		{
			ret[i] = traningTextDir.getPath() +File.separator +classification +File.separator +ret[i];
		}
		return ret;
	}

	public static String getText(String filePath) throws FileNotFoundException,IOException 
	{
	
		InputStreamReader isReader =new InputStreamReader(new FileInputStream(filePath),"GBK");
		BufferedReader reader = new BufferedReader(isReader);
		String aline;
		StringBuilder sb = new StringBuilder();
	
		while ((aline = reader.readLine()) != null)
		{
			sb.append(aline + " ");
		}
		isReader.close();
		reader.close();
		return sb.toString();
	}

	public int getTrainingFileCount()
	{
		int ret = 0;
		for (int i = 0; i < traningFileClassifications.length; i++)
		{
			ret +=getTrainingFileCountOfClassification(traningFileClassifications[i]);
		}
		return ret;
	}

	public int getTrainingFileCountOfClassification(String classification)
	{
		File classDir = new File(traningTextDir.getPath() +File.separator +classification);
		return classDir.list().length;
	}

	public int getCountContainKeyOfClassification(String classification,String key) 
	{
		int ret = 0;
		ret = (int) (classMap.get(classification).containsKey(key) ? classMap.get(classification).get(key):0);
		return ret;
	}
}