package com.Proxy;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import javax.sound.sampled.ReverbType;

//import org.jsoup.helper.HttpConnection;

public class httpTool {
	public static void main(String[] args){
		httpTool get = new httpTool();
		String urlget = "http://125.35.6.41/eucdwan/ApplyServlet?method=readApplyFileData&applyId=f3e2cebe-e4ae-493d-a326-24a4ae90e0c0";
		//String urlget = "http://125.35.6.41/eucdwan/ApplyServlet?method=readApplyFileData&applyId=f3e2cebe-e4ae-493d-a326-24a4ae90e0c0'or 2=2";
		String sql = "' or 2=2";
		//String urlget = "http://125.35.6.41/eucdwan/ApplyServlet?method=readApplyFileData&applyId=f3e2cebe-e4ae-493d-a326-24a4ae90e0c0" + URLEncoder.encode(sql,"utf-8");
		String paramget = "";
		String idurl = "http://127.0.0.1:8775/scan/ffb3163a28a02529/log";
		String Contentget = get.sendGetOrign(idurl, "");
		//String Contentget = get.sendGet(urlget, paramget,null);
		System.out.println(Contentget);
		/*
		String urlpost = "http://www.mogustore.com/search.html";
		String parampost = "keyword=qq";
		String contentPost = get.sendPost(urlpost, parampost);
		System.out.println(contentPost);
		*/
		
	}
	
	//httprequest with the method get
	
	public static String sendGetOrign(String url,String param){
		String result = "";
		BufferedReader in = null;
		try{
			String urlNameString = url + param;
			URL reaUrl = new URL(urlNameString);
			//open the connection with the url
			URLConnection connection = reaUrl.openConnection();
			//set the common property
	//		connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			//build the real connection
			connection.connect();
			//define the BuffereReader to read the respond from url
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while((line = in.readLine()) != null){
				result += line;
			}
		}catch(Exception e){
			System.out.println("send get request with a exception");
			e.printStackTrace();
		}finally{
			try {
				if(in != null){
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
				// TODO: handle exception
			}
		}
		return result;
	}
	
	
	//httprequest with the method get with the information in the log 
	public static String sendGet(String url,String param,RequestPackage pack){
		String result = "";
		BufferedReader in = null;
		int loc;
		try{
			String urlNameString = url + param;
			System.out.println("the request url is:" + urlNameString);
			URL reaUrl = new URL(urlNameString);
			//open the connection with the url
			URLConnection connection = reaUrl.openConnection();
			//set the common property
			
			if(pack == null){
				connection.setRequestProperty("accept", "*/*");
				connection.setRequestProperty("connection", "Keep-Alive");
				connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			}else {
				if(pack.getPacket_proxyconnection() != null){
					loc = pack.getPacket_proxyconnection().indexOf(":");
					System.out.println(pack.getPacket_proxyconnection().substring(0,loc));
					System.out.println(pack.getPacket_proxyconnection().substring(loc+1,pack.getPacket_proxyconnection().length()));
					connection.setRequestProperty(pack.getPacket_proxyconnection().substring(0,loc-1), pack.getPacket_proxyconnection().substring(loc+1,pack.getPacket_proxyconnection().length()));
				}else if(pack.getPacket_accept() != null){
					loc = pack.getPacket_accept().indexOf(":");
					connection.setRequestProperty(pack.getPacket_accept().substring(0,loc-1), pack.getPacket_accept().substring(loc+1,pack.getPacket_accept().length()));
				}else if(pack.getPacket_acceptlanguage() != null){
					loc = pack.getPacket_acceptlanguage().indexOf(":");
					connection.setRequestProperty(pack.getPacket_acceptlanguage().substring(0,loc-1), pack.getPacket_acceptlanguage().substring(loc+1,pack.getPacket_acceptlanguage().length()));
				}else if(pack.getPacket_acceptencoding() != null){
					loc = pack.getPacket_acceptencoding().indexOf(":");
					connection.setRequestProperty(pack.getPacket_acceptencoding().substring(0,loc-1), pack.getPacket_acceptencoding().substring(loc+1,pack.getPacket_acceptencoding().length()));
				}else if(pack.getPacket_connection() != null){
					loc = pack.getPacket_connection().indexOf(":");
					connection.setRequestProperty(pack.getPacket_connection().substring(0,loc-1), pack.getPacket_connection().substring(loc+1,pack.getPacket_connection().length()));
				}else if(pack.getPacket_useragent() != null){
					loc = pack.getPacket_useragent().indexOf(":");
					connection.setRequestProperty(pack.getPacket_useragent().substring(0,loc-1), pack.getPacket_useragent().substring(loc+1,pack.getPacket_useragent().length()));
				}
			}
	
			//build the real connection
			connection.connect();
			//define the BuffereReader to read the respond from url
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while((line = in.readLine()) != null){
				result += line;
			}
		}catch(Exception e){
			System.out.println("send get request with a exception");
			e.printStackTrace();
		}finally{
			try {
				if(in != null){
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
				// TODO: handle exception
			}
		}
		return result;
	}
	
	//httpresquest with the method post
	public static String sendPostOrign(String url,String param){
		OutputStreamWriter out = null;
		BufferedReader in = null;
		String result = "";
		try{
			URL realUrl = new URL(url);
			HttpURLConnection conn = null;
			conn = (HttpURLConnection)realUrl.openConnection();
			
			//send with the post must set two option blow
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			//set the common property
			conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //build the real connection
            conn.connect();
            //get the output of the URLConnecion object
            out = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
            //send the request param
            out.write(param);
            //flush 
            out.flush();
            //define the BuffereReader to read the respond of the URL
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while((line = in.readLine()) != null){
            	result += line;
            }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				if(in != null){
					in.close();
				}
				if(out != null){
					out.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	//httpresquest with the method post
		public static String sendPost(String url,String param,RequestPackage pack){
			OutputStreamWriter out = null;
			BufferedReader in = null;
			String result = "";
			int loc;
			try{
				URL realUrl = new URL(url);
				HttpURLConnection conn = null;
				conn = (HttpURLConnection)realUrl.openConnection();
				
				//send with the post must set two option blow
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestMethod("POST");
				//set the common property
				if(pack == null){
					conn.setRequestProperty("accept", "*/*");
		            conn.setRequestProperty("connection", "Keep-Alive");
		            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				}else{
					if(pack.getPacket_proxyconnection() != null){
						loc = pack.getPacket_proxyconnection().indexOf(":");
						System.out.println(pack.getPacket_proxyconnection().substring(0,loc));
						System.out.println(pack.getPacket_proxyconnection().substring(loc+1,pack.getPacket_proxyconnection().length()));
						conn.setRequestProperty(pack.getPacket_proxyconnection().substring(0,loc-1), pack.getPacket_proxyconnection().substring(loc+1,pack.getPacket_proxyconnection().length()));
					}else if(pack.getPacket_accept() != null){
						loc = pack.getPacket_accept().indexOf(":");
						conn.setRequestProperty(pack.getPacket_accept().substring(0,loc-1), pack.getPacket_accept().substring(loc+1,pack.getPacket_accept().length()));
					}else if(pack.getPacket_acceptlanguage() != null){
						loc = pack.getPacket_acceptlanguage().indexOf(":");
						conn.setRequestProperty(pack.getPacket_acceptlanguage().substring(0,loc-1), pack.getPacket_acceptlanguage().substring(loc+1,pack.getPacket_acceptlanguage().length()));
					}else if(pack.getPacket_acceptencoding() != null){
						loc = pack.getPacket_acceptencoding().indexOf(":");
						conn.setRequestProperty(pack.getPacket_acceptencoding().substring(0,loc-1), pack.getPacket_acceptencoding().substring(loc+1,pack.getPacket_acceptencoding().length()));
					}else if(pack.getPacket_connection() != null){
						loc = pack.getPacket_connection().indexOf(":");
						conn.setRequestProperty(pack.getPacket_connection().substring(0,loc-1), pack.getPacket_connection().substring(loc+1,pack.getPacket_connection().length()));
					}else if(pack.getPacket_useragent() != null){
						loc = pack.getPacket_useragent().indexOf(":");
						conn.setRequestProperty(pack.getPacket_useragent().substring(0,loc-1), pack.getPacket_useragent().substring(loc+1,pack.getPacket_useragent().length()));
					}
				}
				
	            //build the real connection
	            conn.connect();
	            //get the output of the URLConnecion object
	            out = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
	            //send the request param
	            out.write(param);
	            //flush 
	            out.flush();
	            //define the BuffereReader to read the respond of the URL
	            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String line;
	            while((line = in.readLine()) != null){
	            	result += line;
	            }
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					if(in != null){
						in.close();
					}
					if(out != null){
						out.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			return result;
		}
		
		public static String sendPosttoSqlmap (String strUrl,String params){
			System.out.println("the url to post is:" + strUrl);
			System.out.println("the param the url post with is:" + params);
			
			try {
				URL url = new URL(strUrl);
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setUseCaches(false);
				connection.setInstanceFollowRedirects(true);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Accept", "application/json");
				connection.setRequestProperty("Content-Type", "application/json");
				connection.connect();
				OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(),"UTF-8");
				out.append(params);
				out.flush();
				out.close();
				//read the respond
				int length = (int) connection.getContentLength();
				InputStream is = connection.getInputStream();
				if(length != -1){
					byte[] data = new byte[length];
					byte[] temp = new byte[512];
					int readLen = 0;
					int destPos = 0;
					while((readLen = is.read(temp)) > 0){
						System.arraycopy(temp, 0, data, destPos, readLen);
						destPos += readLen;
					}
					String result = new String(data,"UTF-8");
					System.out.println(result);
					return result;
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "error";
		}
}
