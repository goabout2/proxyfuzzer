package com.Proxy;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class Generator {
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
	
	public static int randomNum(int min,int max){
		Random random = new Random();
		int s = random.nextInt(max)%(max-min+1) + min;
		System.out.println(s);
		return s;
	}
	
	public static String randomString(int length){
		StringBuilder builder = new StringBuilder(length);
		for(int i = 0;i <length;i++){
			builder.append((char)ThreadLocalRandom.current().nextInt(65,122));
		}
		String result = builder.toString();
		System.out.println(result);
		return result;
	}
	
	public static void main(String[] args){
		Generator generator = new Generator();
		generator.randomNum(10000, 99999);
		generator.randomString(20);
	}
}
