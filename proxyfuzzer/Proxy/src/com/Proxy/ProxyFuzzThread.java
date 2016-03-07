package com.Proxy;

public class ProxyFuzzThread extends Thread{
	private String targetUrl = null;
	private String targetParam = null;
	
	public ProxyFuzzThread(String targetUrl,String targetParam){
		this.targetUrl = targetUrl;
		this.targetParam = targetParam;
	}
	
	
	public void run(){
		try {
			Fuzzer fuzz = new Fuzzer(targetUrl, targetParam);
			fuzz.Sqlfuzzingwithpackage("GET",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
