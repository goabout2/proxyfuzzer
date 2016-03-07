package com.Proxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class StartOver {
	
	public static void main(String[] args){
		String param = null;

		try{
			for (int i = 0; i < args.length; i++) {
				if(args[i].contains("-p") ||args[i].contains("-pf")){
					System.out.println("the param is:" + args[i] + ",start the proxy");
					System.out.print("the proxy will start with the port 808");
					OutputStream file_S = new FileOutputStream(new File(HttpProxy.LOGFILENAME_S));
					OutputStream file_C = new FileOutputStream(new File(HttpProxy.LOGFILENAME_C));
					HttpProxy.log_S = file_S;
					HttpProxy.log_C = file_C;
					HttpProxy.logging = true;
					if(args[i].contains("-pf")){
						HttpProxy.fuzzing = true;
					}
					HttpProxy.startProxy(808, HttpProxy.class);		
				}if(args[i].contains("-s") || args[i].contains("-s+")){
					System.out.println("the param is:" + args[i] + ",start the spider");
					if(args.length > 1){
						String url = args[i+1];
						Spider spider = new Spider(url);
						spider.getSubUrls(url, url);
					}else{
						System.out.println("spider without url");
					}
					String fileName = "C:\\Users\\zht\\workspace\\Proxy\\log\\crawlUrl.txt";
					FuzzingWithSpider fuzzSpider = new FuzzingWithSpider(fileName);
					fuzzSpider.begin(null, null, args[i]);
				}if(args[i].contains("-l+")){
					System.out.println("the param is:" + args[i] + ",start the log fuzzing");
					String path = "C:\\Users\\zht\\workspace\\Proxy\\log_C.txt";
					File fp = new File(path);
					if(fp.length() == 0){
						System.err.println("the log file is empty,before choicing this option,you first need start the proxy and keep the encouge package");
					}else {
						FuzzingWithLog fuzzlog = new FuzzingWithLog(path);
						fuzzlog.begin(null, args[i]);
					}
				}if(args[i].contains("-h")){
					System.out.println("welcome to the ozkaka,you can ure the follow args to operation this tools" + "\n"
							+ "++++++++++++++++proxy++++++++++++++++++" + "\n"
							+ "the args start a proxy" + "\n"
							+ "-p,the args to start a proxy on the 808 to get http flow" + "\n"
							+ "-pf,the args is same as -p,but is can fuzzing the package when proxy capture a package at the same time" + "\n"
							+ "++++++++++++++++spider++++++++++++++++++" + "\n"
							+ "the args start a spider to crawl the url from a specirfy webstation" + "\n"
							+ "-s http://www.baidu.com crawl the url from http://www.baidu.com" + "\n"
							+ "-s+s http://www.baidu.com crawl the url from http:/www.baudu.com and fuzzing with sqlinject module" + "\n"
							+ "-s+y http://www.baidu.com crawl the url from http:/www.baudu.com and fuzzing with yuanquan module" + "\n"
							+ "-s+x http://www.baidu.com crawl the url from http:/www.baudu.com and fuzzing with xss module" + "\n"
							+ "++++++++++++++++logfuzzing+++++++++++++++++" + "\n"
							+ "the args read the package record by proxy and user these package to fuzzing" + "\n"
							+ "-l+s,read the logrecord and fuzzing with the sqlinject module" + "\n"
							+ "-l+y,read the logrecord and fuzzing with the yuequan module" + "\n");
				}
				//System.out.println(i + "params is:" + args[i]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
