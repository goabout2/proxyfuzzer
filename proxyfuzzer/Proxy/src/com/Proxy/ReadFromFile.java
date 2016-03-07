package com.Proxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//http://www.cnblogs.com/lovebread/archive/2009/11/23/1609122.html
public class ReadFromFile {
	/*read the file with bytes,common for read the binary file*,such as picture,video*/
	public static void readFileByBytes(String fileName){
		File file = new File(fileName);
		InputStream in = null;
		try{
			System.out.println("read the file with the bytes,one time one bytes");
			in = new FileInputStream(file);
			int tempbyte;
			while((tempbyte = in.read()) != -1){
				System.out.write(tempbyte);
			}
			in.close();
		}catch(IOException e){
			e.printStackTrace();
			return;
		}
		
		try{
			System.out.println("read the file with the bytes,one time more bytes");
			byte[] tempbytes = new byte[100];
			int byteread = 0;
			in = new FileInputStream(fileName);
			
			//ReadFromFile.showAvailableBytes(in);
			while((byteread = in.read(tempbytes)) != -1){
				System.out.write(tempbytes,0,byteread);
			}
		}catch(Exception e1){
			e1.printStackTrace();
		}finally{
			if(in != null){
				try{
					in.close();
				}catch (IOException e1){
					
				}
			}
		}
	}
	
	/*read the file with line,common for read the file which with the format*/
	public static ArrayList readFileByLines(String fileName){
		File file = new File(fileName);
		BufferedReader reader = null;
		ArrayList fuzz = new ArrayList();
		try{
			System.out.println("read the file with line,one time one line");
			reader = new BufferedReader(new FileReader(file));
			String temString = null;
			int line = 1;
			int i = 0;
			while((temString = reader.readLine()) != null){
				//System.out.print("line" + line + temString + "\n");
				fuzz.add(temString);
				System.out.print("line" + line + fuzz.get(i) + "\n");
				i++;
				line++;
			}
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if (reader != null){
				try{
					reader.close();
				}catch(IOException e1){
					
				}
				
			}
		}
		return fuzz;
	}
	
	/*read the log file with package,for fuzz with log*/
	public static String[] readFileByPackage(String fileName){
		File file = new File(fileName);
		BufferedReader reader = null;
		String[] words = null;
		
		try{
			System.out.println("read the file with package,one time one package");
			reader = new BufferedReader(new FileReader(file));
			String temString = null;
			int line = 1;
			int i = 0;
			
			String packA = null;
			String packB = null;
			
			while((temString = reader.readLine()) != null){
				packA = packA + temString + "==";
				i++;
				line++;
			}
			System.out.println("packA" + packA);
			
			String regEx = "GET|POST|CONNECT";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(packA);
			
			words = p.split(packA);
			if(words.length > 0){
				int cout = 0;
				while(cout < words.length)
				{
					if(m.find())
					{
						words[cout] = m.group() + words[cout];
					}
					cout++;
				}
			}
			
			/*
			for(int index = 0;index < words.length;index++){
				System.out.println(words[index]);
			}
			*/
			
			/*
		    String[] strarray=packA.split("GET|POST|CONNECT"); 
		      for (int j = 0; j < strarray.length; j++) 
		          System.out.println(strarray[j]);
		          */
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return words;
	}
	

	public static void main(String[] args)throws IOException{
		ReadFromFile read = new ReadFromFile();
		//String filename = "C:\\Users\\zht\\workspace\\Proxy\\payloads\\sqli.txt";
		//read.readFileByLines(filename);
		String filename = "C:\\Users\\zht\\workspace\\Proxy\\log_C.txt";
		FuzzingWithLog log = new FuzzingWithLog(filename);
		
		
		String []words = read.readFileByPackage(filename);
		RequestPackage pac = new RequestPackage();
		for(int index = 0;index < words.length;index++){
			System.out.println(words[index]);
			//pac = log.PrasePackage(words[index]);
		}
		
		/*
		String str="put 19 put216 put801"; 
	    String[] strarray=str.split("put"); 
	      for (int i = 0; i < strarray.length; i++) 
	          System.out.println(strarray[i]); */
	}
}
