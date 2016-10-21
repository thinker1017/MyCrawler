package com.tcl.crawler.Classifier;


import com.tcl.crawler.Classifier.ChineseSpliter;
import com.tcl.crawler.Classifier.PriorProbability;
import com.tcl.crawler.Classifier.TrainingDataManager;
import com.tcl.crawler.Classifier.StopWordsHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BayesClassifier 
{
	private static final Logger LOG = LoggerFactory.getLogger(BayesClassifier.class);
	private static TrainingDataManager tdm;//ѵ����������
	//private String trainnigDataPath;//ѵ����·��
	private static double zoomFactor = 65.0f;
	private static BayesClassifier bayesClassifier = new BayesClassifier();
	/**
	* Ĭ�ϵĹ���������ʼ��ѵ����
	*/
	private BayesClassifier() 
	{
		tdm =TrainingDataManager.getInstance();
	}

	/**
	* �������ı���������X�ڸ�ķ���Cj�е�����������
	* <code>ClassConditionalProbability</code>����ֵ
	* @param X ����ı���������
	* @param Cj ������
	* @return ����������������ֵ����<br>
	*/
	float calcProd(String[] X, String Cj) 
	{
		float ret = 1.0F;
		for (int i = 0; i <X.length; i++)
		{
			String Xi = X[i];
			ret *=ClassConditionalProbability.calculatePxc(Xi, Cj)*zoomFactor;
		}
		ret *= PriorProbability.calculatePc(Cj);
		return ret;
	}
	/**
	* ȥ��ͣ�ô�
	* @param text ����ı�
	* @return ȥͣ�ôʺ���
	*/
	public String[] DropStopWords(String[] oldWords)
	{
		Vector<String> v1 = new Vector<String>();
		for(int i=0;i<oldWords.length;++i)
		{
			if(StopWordsHandler.IsStopWord(oldWords[i])==false)
			{//����ͣ�ô�
				v1.add(oldWords[i]);
			}
		}
		String[] newWords = new String[v1.size()];
		v1.toArray(newWords);
		return newWords;
	}
	/**
	* �Ը���ı����з���
	* @param text ����ı�
	* @return ������
	*/
	public String classify(String text) 
	{
		String[] terms = null;
		terms= ChineseSpliter.split(text, " ").split(" ");//���ķִʴ���(�ִʺ�����ܻ�����ͣ�ôʣ�
		terms = DropStopWords(terms);//ȥ��ͣ�ôʣ�����Ӱ�����
		
		String[] Classes = tdm.getTraningClassifications();//����
		float probility = 0.0F;
		List<ClassifyResult> crs = new ArrayList<ClassifyResult>();//������
		LOG.info("classifying"+text.substring(0, 50));
		for (int i = 0; i <Classes.length; i++) 
		{
			String Ci = Classes[i];//��i������
			probility = calcProd(terms, Ci);//�������ı���������terms�ڸ�ķ���Ci�еķ�����������
			ClassifyResult cr = new ClassifyResult();
			cr.classification = Ci;//����
			cr.probility = probility;//�ؼ����ڷ������������
			//System.out.println("In process....");
			LOG.debug(Ci + "��" + probility);
			crs.add(cr);
		}
		java.util.Collections.sort(crs,new Comparator<ClassifyResult>() 
		{
			public int compare(final ClassifyResult o1,final ClassifyResult o2) 
			{
				final ClassifyResult m1 = (ClassifyResult) o1;
				final ClassifyResult m2 = (ClassifyResult) o2;
				final double ret = m1.probility - m2.probility;
				if (ret < 0) 
				{
					return 1;
				} 
				else 
				{
					return -1;
				}
			}
		});
		return crs.get(0).classification;
	}
	
	public static void main(String[] args)
	{
		String text="";
		BayesClassifier classifier = new BayesClassifier();//����Bayes������
		String result = classifier.classify(text);//���з���
		System.out.println("["+result+"]");
	}
	

	/**
	 * @return
	 */
	public static BayesClassifier getInstance() {
		return bayesClassifier;
	}
}