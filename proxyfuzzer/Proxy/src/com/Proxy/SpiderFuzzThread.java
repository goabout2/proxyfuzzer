package com.Proxy;

public class SpiderFuzzThread {
	String targetUrl = null;
	String targetParam = null;
	String flag = null;
	String type = null;
	
	public SpiderFuzzThread(String targetUrl,String targetParam,String flag,String type){
		this.targetUrl = targetUrl;
		this.targetParam = targetParam;
		this.flag = flag;
		this.type = type;
	}
	
	public void run(){
		try{
			Fuzzer fuzzer = new Fuzzer(targetUrl,targetParam);
			if(type.contains("-s+s")){
				System.out.println("fuzzing with the url crawl by the spider with the sql model");
				fuzzer.Sqlfuzzingwithpackage(flag,null);
			}if(type.contains("-s+y")){
				System.out.println("fuzzing with the url crawl by the spider with the yuequan model");
				fuzzer.Yewufuzzingwithpackage(flag, null);
			}if(type.contains("-s+x")){
				System.out.println("fuzzing with the url crawl by the spider with the xss model");
				fuzzer.Xssfuzzing("GET", null);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
