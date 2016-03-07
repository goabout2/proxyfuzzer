package com.Proxy;

import java.io.IOException;
import java.io.RandomAccessFile;

public class AppendToFile {
	/*append to a file with RandomAccessFile*/
	public static void appendMethodA(String fileName,String content){
		try{
			//open a random access filestream
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			//the length of the file 
			long fileLength = randomFile.length();
			//move the file seek to end of the file
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
			randomFile.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
