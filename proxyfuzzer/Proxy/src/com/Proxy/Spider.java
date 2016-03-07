package com.Proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.Document;


import org.jsoup.Jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Spider {
	private static Map<String,String>absRelativeUrlMap = new HashMap<String, String>();
	private static Map<String, String>filterMap = new HashMap<String, String>();
	private final static int timeOut = 30000;
	private static String rootUrl = null;
	
	public Spider(String rootUrl){
		this.rootUrl = rootUrl;
	}
	
	public static boolean filterTheUrl(String url){
		boolean result = false;
		String tmp = null;
		String rp = "=&";
		String regdex = "=([^&]*)(&|$)";
		
		try{
			if(url.contains("?")){
				Pattern p = Pattern.compile(regdex);
				Matcher m = p.matcher(url);
				String tmpurl = url;
				while(m.find()){
					tmp = m.group();
					System.out.println(tmp);
					if(tmp.contains("&")){
						tmpurl = tmpurl.replace(tmp, rp);
						System.out.println(tmpurl);
					}else{
						tmpurl = tmpurl.replace(tmp, "=");
					}
				}
				System.out.println("the tmpurl is :" + tmpurl);
				if(filterMap.put(tmpurl, url) == null){
					result = true;
				}else {
					result = false;
				}
			}else{
				return true;
			}
		}catch(Exception e){		
		}		
		return result;
	}
	
	public static String simulateLogin(String loginUrl,String userName,String passWord){
		String cookit = null;
		String param = "";
		String result = "";
		ArrayList inputList = new ArrayList();
		Map<String, String>paramMap = new HashMap<String, String>();
		httpTool http = new httpTool();
		
		try{
			org.jsoup.nodes.Document doc = Jsoup.connect(loginUrl).get();
			Elements list = doc.select("input[name]");
			for(Element link : list){
				String name = link.attr("name");
				if(link.attr("value") != null){
					paramMap.put(name,link.attr("value"));
				}else{
						paramMap.put(name,null);
				}
				System.out.println(name);
			}
			
			Iterator it = paramMap.keySet().iterator();
			while(it.hasNext()){
				String key = it.next().toString();
				String vaule = paramMap.get(key);
				if(key.contains("user")){
					vaule = userName;
				}if(key.contains("pass")){
					vaule = passWord;
				}
				param += key + "=" + vaule + "&";
			}
			param = param.substring(0,param.length()-1);
			System.out.println(param);
			result = http.sendPostOrign(loginUrl, param);
			System.out.println(result);
		}catch(Exception e){
			e.printStackTrace();
		}
		return cookit;
	}
	
	public static void getSubUrls(String absUrl,String relativeUrl){
		if(absRelativeUrlMap.get(absUrl) != null){
			return;
		}
		System.out.println(absUrl);
		absRelativeUrlMap.put(absUrl, relativeUrl);
		
		AppendToFile logUrl = new AppendToFile();
		String logPath = "C:\\Users\\zht\\workspace\\Proxy\\log\\crawlUrl.txt";
		if(filterTheUrl(absUrl)){
			logUrl.appendMethodA(logPath, absUrl + "\n");
		}else{
			System.out.println("the url :" + absUrl + "is repeat");
		}
		
		//logUrl.appendMethodA(logPath, relativeUrl + "\n");
		//logUrl.appendMethodA(logPath, "+++++++++++");
		org.jsoup.nodes.Document doc = null;
		try{
			doc = Jsoup.connect(absUrl).timeout(timeOut).get();
		}catch(Exception e){
			System.err.println("url = " + absUrl + ",the page is fail");
			return;
		}
		Elements eles = doc.body().select("a[href]");
		for(Element ele:eles){
			String absHref = ele.attr("abs:href").replaceAll("\\.\\.\\/", "");
			String href = ele.attr("href");
			if(href.startsWith("javascript") ||
					href.startsWith("#") ||
					(href.contains("(") && href.contains(""))){
				continue;
			}
			if(absHref.startsWith(rootUrl)){
				getSubUrls(absHref, href);
			}
		}
	}
	
	public static void main(String[] args){
		/*
		String url = "http://bbs.pediy.com/";
		String test = "the fist is:<a href=\"http://blog.csdn.net/lyjluandy\">lyjluandy的专�?</a>";
		Spider spider = new Spider("bbs.pediy.com");
		
		Set<String> urlList = new HashSet<String>();
		urlList = spider.BlackWidow(url);
		Iterator<String>it =urlList.iterator();
		while(it.hasNext()){
			String str = it.next();
			System.out.println(str);
		}
	
		
		for(int i = 0;i < urlList.size();i++){
			System.out.println(urlList.);
		}
		//spider.getUrl(test);
		*/
		
		String urlforspider = "http://bbs.pediy.com/";
		Spider spider = new Spider(urlforspider);
		spider.getSubUrls(urlforspider, urlforspider);
		//spider.simulateLogin("https://passport.csdn.net/account/login","goabout2","gosaking2");
		
		
		//String url = "http://m.app.mi.com/searchapi?pageIndex=0&pageSize=20&keywords=weixin";
		//spider.filterTheUrl(url);
		
		//map test
		/*
		Map<String, String>filer = new HashMap<String, String>();	
		System.out.println(filer.put("001", "aa"));
		System.out.println(filer.put("001", "bb"));
		System.out.println(filer.get("001"));
		*/
		
	}
}
