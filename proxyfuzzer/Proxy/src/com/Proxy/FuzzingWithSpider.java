package com.Proxy;

import java.net.URLDecoder;
import java.util.ArrayList;

public class FuzzingWithSpider {
	String fileName = null;
	
	public FuzzingWithSpider(String fileName){
		this.fileName = fileName;
	}
	
	//temporary for the get method
	public void begin(String specify,String flag,String type){
		ReadFromFile read = new ReadFromFile();
		ArrayList log =  new ArrayList();
		String tmp = null;
		String url = null;
		String param = null;     
		try{
			log = read.readFileByLines(fileName);
			for(int i = 0;i < log.size();i++){
				if(specify == null){
					tmp = log.get(i).toString();
					System.out.println(tmp);
					tmp = URLDecoder.decode(tmp,"UTF-8");
					if(tmp.contains("?")){
						int loc = tmp.indexOf("?");
						url = tmp.substring(0,loc+1);
						param = tmp.substring(loc+1,tmp.length());
						System.out.println("the url is:" + url);
						System.out.println("the param is:" + param);
						SpiderFuzzThread spdierThread = new SpiderFuzzThread(url, param, "GET", type);
						spdierThread.run();
					}else{
						continue;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		String fileName = "C:\\Users\\zht\\workspace\\Proxy\\log\\crawlUrl.txt";
		FuzzingWithSpider spide = new FuzzingWithSpider(fileName);
		spide.begin(null, null,"-s+s");
	}
}
