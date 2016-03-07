package com.Proxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

//import jjjjjjjj.HttpProxy;

public class HttpProxy extends Thread{
	static public int CONNECT_RETRIES=5;      //the time to connect to the target
    static public int CONNECT_PAUSE=5;        //the time between the two connect
    static public int TIMEOUT=50;             //the max time to try connect
    static public int BUFSIZ=1024;            //the size of the buffer
    static public boolean logging = false;    //keep the log or not
    static public OutputStream log=null;      
    
    static public OutputStream log_S=null;    //the flow of the log return from server 
    static public OutputStream log_C=null;    //the flow of the log send by client
    static public String LOGFILENAME_S="log_S.txt";
    static public String LOGFILENAME_C="log_C.txt";
    static public boolean fuzzing = false;
    
    // ���������õ�Socket
    protected Socket socket;                  //the socket for connect to the client
    // �ϼ��������������ѡ
    static private String parent=null;
    static private int parentPort=-1;
    static public void setParentProxy(String name, int pport) {
	parent=name;
	parentPort=pport;
    }

    // �ڸ���Socket�ϴ���һ�������̡߳�
    public HttpProxy(Socket s) { socket=s; start(); }

    public void writeLog(int c, boolean browser) throws IOException {
    //System.out.println("+++++");
    if(browser) log_C.write((char)c);
    else log_S.write((char)c);
	//System.out.println("+++++");
    }

    public void writeLog(byte[] bytes,int offset, int len, boolean browser) throws IOException {
    	  for (int i=0;i<len;i++) 
              writeLog((int)bytes[offset+i],browser);
    }

    // Ĭ������£���־��Ϣ�����
    // ��׼����豸
    // ��������Ը�����
    public String processHostName(String url, String host, int port, Socket sock) {
	java.text.DateFormat cal=java.text.DateFormat.getDateTimeInstance();
	System.out.println("------");
	System.out.println(cal.format(new java.util.Date()) + " - " + url + " " 
              + sock.getInetAddress()+"\n");
	System.out.println("------");
	return host;
    }

    // ִ�в������߳�
    public void run() {
	String line;
	String host;              //the host of the target
	int port=80;
        Socket outbound=null;
	try {
	    socket.setSoTimeout(TIMEOUT);
	    InputStream is=socket.getInputStream();
	    OutputStream os=null;
	    try {
                // ��ȡ�����е�����
		line="";
		host="";
		int state=0;
		boolean space;
		while (true) {
		    int c=is.read();
		    if (c==-1) break;
		    if (logging) writeLog(c,true);
		    space=Character.isWhitespace((char)c); 
		    
		    switch (state) {
		    
		    case 0:
			if (space) continue; 
		        state=1;
		        
		    case 1:
			if (space) {
			    state=2;
			    continue;
			}
			line=line+(char)c;
			//System.out.println("the line is:" + line);
			break;
			
		    case 2:
			if (space) continue; // ��������հ��ַ�
  		        state=3;
  		        
		    case 3:
			if (space) {
			    state=4; 
                            // ֻȡ���������Ʋ���
			    String host0=host;
			    int n;
			    n=host.indexOf("//");
			    if (n!=-1) host=host.substring(n+2);
			    n=host.indexOf('/');
			    if (n!=-1) host=host.substring(0,n);
                // �������ܴ��ڵĶ˿ں�
			    n=host.indexOf(":");
			    if (n!=-1) { 
				port=Integer.parseInt(host.substring(n+1));
				host=host.substring(0,n);
			    }
			    host=processHostName(host0,host,port,socket);
			    
			    //show the requestMethod and resquestUrl
			    System.out.println("the methdo is:" + line);
			    System.out.println("requestUrl:" + host0);
			    
			    if(fuzzing){
			    	if(line.contentEquals("GET")){
				    	int tag = host0.indexOf("?");
				    	String hostUrl = host0.substring(0,tag);
				    	String hostParam = host0.substring(tag+1,host0.length());
				    	ProxyFuzzThread getFuzzing = new ProxyFuzzThread(hostUrl, null);
				    	getFuzzing.start();
				    }
			    }
			    
			    //user the url to fuzzing
			    /*
			    ForGetMethod getFuzzing = new ForGetMethod(host0, null);
			    getFuzzing.start();
			    */
			    
			    if (parent!=null) {
				host=parent;
				port=parentPort;
			    }
			    int retry=CONNECT_RETRIES;
			    while (retry--!=0) {
				try {
				    outbound=new Socket(host,port);
				    break;
				} catch (Exception e) { }
                                // �ȴ�
				Thread.sleep(CONNECT_PAUSE);
			    }
			    if (outbound==null) break;
			    outbound.setSoTimeout(TIMEOUT);
			    os=outbound.getOutputStream();
			    os.write(line.getBytes());
			    os.write(' ');
			    os.write(host0.getBytes());
			    os.write(' ');
			    pipe(is,outbound.getInputStream(),os,socket.getOutputStream());
			    break;
			}
			
			host=host+(char)c;
			//System.out.println("the host is:" + host);
			break;
		    }
		}
	    }
	    catch (IOException e) { }

    } catch (Exception e) { }
    finally {
		    try { socket.close();} catch (Exception e1) {}
		    try { outbound.close();} catch (Exception e2) {}
       }
    }


    void pipe(InputStream is0, InputStream is1,
         OutputStream os0,  OutputStream os1) throws IOException {
	try {
	    int ir;
	    byte bytes[]=new byte[BUFSIZ];
	    while (true) {
		try {
		    if ((ir=is0.read(bytes))>0) {
		    	
		    String tmpClient = new String(bytes,"ascii"); 
		    System.out.println(">>>>" + tmpClient + ">>>>");
		    
			os0.write(bytes,0,ir);
			if (logging) writeLog(bytes,0,ir,true);
		    }
		    else if (ir<0)
			break;
		} catch (InterruptedIOException e) { }
		try {
		    if ((ir=is1.read(bytes))>0) {
		    /*
		    String tmpServer = new String(bytes,"ascii"); 
		    System.out.println("<<<<" + tmpServer);
		    */
			os1.write(bytes,0,ir);
			if (logging) writeLog(bytes,0,ir,false);
		    }
		    else if (ir<0) 
			break;
		} catch (InterruptedIOException e) { }
	    }
	} catch (Exception e0) {
	    System.out.println("Pipe�쳣: " + e0);
	}
    }


    static public void startProxy(int port,Class clobj) {
	ServerSocket ssock;
	Socket sock;
        try {
	    ssock=new ServerSocket(port);
	    while (true) {
		Class [] sarg = new Class[1];
		Object [] arg= new Object[1];
		sarg[0]=Socket.class;
		try {
		    java.lang.reflect.Constructor cons = clobj.getDeclaredConstructor(sarg);
		    arg[0]=ssock.accept();
		    cons.newInstance(arg); // ����HttpProxy�����������ʵ��
		} catch (Exception e) { 
		    Socket esock = (Socket)arg[0];
		    try { esock.close(); } catch (Exception ec) {}
		}
	    }
	} catch (IOException e) {
	}
    }

    // �����õļ�main����
    static public void main(String args[]) throws FileNotFoundException{
    	System.out.println("�ڶ˿�808�������������\n");
    	OutputStream file_S=new FileOutputStream(new File(LOGFILENAME_S)); 
        OutputStream file_C=new FileOutputStream(new File(LOGFILENAME_C)); 
        HttpProxy.log_S=file_S; 
        HttpProxy.log_C=file_C; 
        HttpProxy.logging=true; 
	HttpProxy.startProxy(808,HttpProxy.class);
      }
}
