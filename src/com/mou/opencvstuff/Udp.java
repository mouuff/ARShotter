package com.mou.opencvstuff;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;

import android.os.Build.VERSION;
import android.util.Log;

public class Udp
{
	DatagramSocket s;
	InetAddress local;

	public void udpSend(String server,int server_port,String messageStr){
		try
		{
			s = new DatagramSocket();
		}
		catch (SocketException e)
		{
			Log.e("UDP", e.getMessage());
		}
		try
		{
			local = InetAddress.getByName(server);
		}
		catch (UnknownHostException e){}
		int msg_length = messageStr.length();
		byte[] message = messageStr.getBytes();
		DatagramPacket p = new DatagramPacket(message, msg_length,local,server_port);
		try
		{
			s.send(p);
		}
		catch (IOException e){
			Log.e("UDP", e.getMessage());
		}

	}
}

/*
public class Udp {
	private InetAddress addr;
	private DatagramSocket sock;
	protected int port;
	
	public Udp(String addr_, int port_) throws SocketException, UnknownHostException
	{
		port = port_;
		//sock = new DatagramSocket(port);
		
		sock = new DatagramSocket(null);
	    sock.setReuseAddress(true);
	    sock.setBroadcast(true);
	    sock.bind(new InetSocketAddress(12345));
	    
		addr = InetAddress.getByName(addr_);
	}
	public void send(String message) throws IOException
	{
		DatagramPacket packet;
		
		packet = new DatagramPacket(
				message.getBytes(),
				message.length(),
				addr,
				port);
		sock.send(packet);
	}
}
*/