package com.tcl.crawler.Classifier;

public class ClassifierTest {

	public void test() {
		BayesClassifier classifier = BayesClassifier.getInstance();
		long start = System.currentTimeMillis();
		String text = "����˵���ܵ�ֱ�Ӵ̼������˳����߶˺ͼ�����ѻ�������ص�Ӱ�죬������������г���Ȼ�������������Ż����������ǽ�������Ҳ��������������أ�ȫ���ܵ�����ǰ�ͺ�ߣ���������Ŀǰ���������Ӧ��˵�ǱȽ���Ҳ�ȽϽ�����";
		String result = classifier.classify(text);
		long use = System.currentTimeMillis() - start;
		System.out.println("use"+use+"ms");
		System.out.println("��������["+result+"]");
		 start = System.currentTimeMillis();
		 text = "�����ͱ���һ��ʼ����Ƿ�ѣ��������Ϻ���ʦ�����ī�ơ��޲�ѷ�������ھ����й�±���ȡФ��(΢��) ��ڣ�Ӯ��ְҵ���ĵ��߸�������ھ������������������޹ڵ�ħ�䡣������ԱѲ����EPTC5��ɱ������ϧ�������-��������Ǿ�˷�ӡ�����������4-3��̭ƽ��˹������4-3��������˹�������ͺ�ϣ��˹��5�ν���Ӯ��4�Σ����������繫����0-5�Ұܡ�";
		 result = classifier.classify(text);
		 use = System.currentTimeMillis() - start;
		System.out.println("use"+use+"ms");
		System.out.println("��������["+result+"]");
	
	}
}
