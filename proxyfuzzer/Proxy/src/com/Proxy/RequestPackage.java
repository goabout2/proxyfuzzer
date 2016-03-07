package com.Proxy;

public class RequestPackage {
	String Packet_method = null;
	String Packet_url = null;
	String Packet_protocol = null;
	String Packet_host = null;
	String Packet_proxyconnection = null;
	String Packet_accept = null;
	String Packet_acceptlanguage = null;
	String Packet_acceptencoding = null;
	String Packet_dnt = null;
	String Packet_connection = null;
	String Packet_useragent = null;
	
	public String getPacket_method(){
		return Packet_method;
	}
	
	public String getPacket_protocol(){
		return Packet_protocol;
	}
	
	public String getPacket_url(){
		return Packet_url;
	}
	
	public String getPacket_host(){
		return Packet_host;
	}
	
	public String getPacket_proxyconnection(){
		return Packet_proxyconnection;
	}
	
	public String getPacket_useragent(){
		return Packet_useragent;
	}
	
	public String getPacket_accept(){
		return Packet_accept;
	}
	
	public String getPacket_acceptlanguage(){
		return Packet_acceptlanguage;
	}
	
	public String getPacket_acceptencoding(){
		return Packet_acceptencoding;
	}
	
	public String getPacket_dnt(){
		return Packet_dnt;
	}
	
	public String getPacket_connection(){
		return Packet_connection;
	}
	
	
	public void setPacket_metheod(String method){
		this.Packet_method = method;
	}
	
	public void sePacket_protocol(String protocol){
		this.Packet_protocol = protocol;
	}
	
	public void sePacket_url(String url){
		this.Packet_url = url;
	}
	
	public void setPacket_host(String host){
		this.Packet_host = host;
	}
	
	public void setPacket_proxyconnection(String proxyconnection){
		this.Packet_proxyconnection = proxyconnection;
	}
	
	public void setPacket_useragent(String useragent){
		this.Packet_useragent = useragent;
	}
	
	public void setPacket_accept(String accept){
		this.Packet_accept = accept;
	}
	
	public void setPacket_acceptlanguage(String acceptlanguage){
		this.Packet_acceptlanguage = acceptlanguage;
	}
	
	public void setPacket_acceptencoding(String acceptencoding){
		this.Packet_acceptencoding = acceptencoding;
	}
	
	public void setPacket_dnt(String dnt){
		this.Packet_dnt = dnt;
	}
	
	public void setPacket_connection(String connection){
		this.Packet_connection = connection;
		
	}
	
}
