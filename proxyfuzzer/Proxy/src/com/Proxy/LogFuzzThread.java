package com.Proxy;

import java.util.HashMap;
import java.util.Map;

public class LogFuzzThread extends Thread{
	private String targetUrl = null;
	private String targetParam = null;
	private String taskId = null;
	public static Map<String, String>urlIdmap = new HashMap<String,String>();
	private RequestPackage pack;
    private String flag = null;
    private String type = null;
    
	
	public LogFuzzThread(String targetUrl,String targetParam,RequestPackage pack,String flag,String type){
		this.targetUrl = targetUrl;
		this.targetParam = targetParam;
		this.pack = pack;
		this.flag = flag;
		this.type = type;
	}
	
	public void run(){
		try{
			Fuzzer fuzz = new Fuzzer(targetUrl, targetParam);
			if(type.contains("y")){
				System.out.println("fuzzing with log use the yuanquan model");
				fuzz.Yewufuzzingwithpackage(flag, pack);
			}if(type.contains("s")){
				System.out.println("fuzzing with log use the sql model");
				//fuzz.Sqlfuzzingwithpackage(flag, pack);
				
				taskId = fuzz.Sqlfuzzingwithsqlmap(flag, null);
				urlIdmap.put(taskId, targetUrl+targetParam);
				
			}if(type.contains("x")){
				System.out.println("fuzzing with log use the xss model");
				fuzz.Xssfuzzing("GET", null);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
