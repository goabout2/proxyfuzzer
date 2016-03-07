package com.Proxy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Spliterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FuzzingWithLog{
	String fileName = null;
	Map<String, String> urlIdmap = new HashMap<String, String>();

	public FuzzingWithLog(String logPath){
		this.fileName = logPath;
	}
	
	/*
	public RequestPackage PrasePackage(String pack){
		RequestPackage request = new RequestPackage();
		//System.out.println("XXXXXXXXXXXXOOOOOOOOOOOOOOOO"+pack);
		String tmp = null;
		try{
			if(pack.startsWith("GET")){
				request.setPacket_metheod("GET");
			}else if (pack.startsWith("POST")){
				request.setPacket_metheod("POST");
			}else{
				return null;
			}
			int A,B;
			if(pack.indexOf("http://") != -1){
				A = pack.indexOf("http://");
			
				B = pack.indexOf("HTTP");
				if(B == -1){
					return null;
				}else{
					request.sePacket_url(pack.substring(A,B));
					request.sePacket_protocol(pack.substring(B,B+8));
					tmp = pack.substring(B,pack.length());
				}
				
				
				//Pattern p = Pattern.compile("==([^&]*)(==|$)");
				//Matcher m = p.matcher(tmp);
				//while(m.find()){
					//System.out.println(m.group());
				//}
			
	
				String[] spilt = tmp.split("==");
				for(int i = 0;i < spilt.length;i++){
					//System.out.println(spilt[i]);
					if(spilt[i].contains("Host")){
						request.setPacket_host(spilt[i]);
					}else if(spilt[i].contains("Proxy-Connection")){
						request.setPacket_proxyconnection(spilt[i]);
					}else if(spilt[i].contains("Accept:")){
						request.setPacket_accept(spilt[i]);
					}else if(spilt[i].contains("Accept-Encoding")){
						request.setPacket_acceptencoding(spilt[i]);
					}else if(spilt[i].contains("Accept-Language")){
						request.setPacket_acceptlanguage(spilt[i]);
					}else if(spilt[i].contains("Connection")){
						request.setPacket_connection(spilt[i]);
					}else if(spilt[i].contains("User-Agent")){
						request.setPacket_useragent(spilt[i]);
					}
				}
				
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return request;
	}
	
	*/
	//read the log record by the proxy,and fuzzing the url with a thread 
	public void begin(String specify,String type){
		ReadFromFile read = new ReadFromFile();
		RequestPackage prasedPackage = new RequestPackage();
		Generator genertor = new Generator();
		String[] log = read.readFileByPackage(fileName);
		String url = null;
		String param = null;
		try{
			for(int i = 0;i < log.length;i++){
				prasedPackage = genertor.PrasePackage(log[i]);
				if(prasedPackage == null){
					continue;
				}else{
					String tmp = prasedPackage.getPacket_url();
				    int loc;
				    if(tmp.indexOf("?") != -1){
				    	loc = tmp.indexOf("?");
				    	url = tmp.substring(0,loc+1);
				    	param = tmp.substring(loc+1,tmp.length());
				    }else {
						continue;
					}
				    String dome = url.substring(0,url.indexOf("com")+3);
				    if(specify == null){
				    	if(prasedPackage.getPacket_method() == "GET"){
				    		System.out.println("im the " + i + "package+++++++++++++++++++++++++++++++++++");
					    	LogFuzzThread logFuzz = new LogFuzzThread(url, param, prasedPackage, "GET",type);
					    	logFuzz.start();
					    	logFuzz.join();
					    	urlIdmap.putAll(logFuzz.urlIdmap);
					    	System.out.println("xxoo ooxx" + urlIdmap);
					    	
					    }else if(prasedPackage.getPacket_method() == "POST"){
					    	System.out.println("im the " + i + "package+++++++++++++++++++++++++++++++++++");
					    	LogFuzzThread logFuzz = new LogFuzzThread(url, param, prasedPackage, "POST",type);
					    	logFuzz.start();
					    }
				    }else{
				    	if(specify == dome){
				    		if(prasedPackage.getPacket_method() == "GET"){
						    	LogFuzzThread logFuzz = new LogFuzzThread(url, param, prasedPackage, "GET",type);
						    	logFuzz.start();
						    }else if(prasedPackage.getPacket_method() == "POST"){
						    	LogFuzzThread logFuzz = new LogFuzzThread(url, param, prasedPackage, "POST",type);
						    	logFuzz.start();
						    }
				    	}else {
							continue;
						}
				    }
				    
				}
			    
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)throws IOException{
		String path = "C:\\Users\\zht\\workspace\\Proxy\\log_C.txt";
		FuzzingWithLog fuzzing = new FuzzingWithLog(path);
		fuzzing.begin(null,"-ly");
		
		String test = "100318686";
		int j = Integer.parseInt(test); 
		System.out.println(j);
	}
}
