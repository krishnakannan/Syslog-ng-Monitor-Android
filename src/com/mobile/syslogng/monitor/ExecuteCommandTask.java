package com.mobile.syslogng.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


@SuppressLint("TrulyRandom")
public class ExecuteCommandTask extends AsyncTask<String, Void, String>{

	private Context context;
	private String hostName;
	private Integer portNumber;
	private String command;
	private IExecuteCommandCallBack callBack;
	
	private SSLSocket socket;
	private SSLSession session;
	private PrintWriter out;
	private StringBuilder sBuilder = null;
	private BufferedReader in;
	private String line;
	private Boolean isException = false;
	
	
	public ExecuteCommandTask(){
		// Empty Constructor
	}
	
	public ExecuteCommandTask(IExecuteCommandCallBack callBack, Context context, String hostName, Integer portNumber, String command){
		
		this.callBack = callBack;
		this.context = context;
		this.hostName = hostName;
		this.portNumber = portNumber;
		this.command = command;
		
	}
	
	@Override
	 protected void onPreExecute() {
	  super.onPreExecute();
	  callBack.commandExecutionStart();
	 }
	
	@Override
	protected String doInBackground(String... params) {
		
		String result = null;
		//Implementing for SELFSIGNED CERTIFICATES - Will be changed in future as per needs
		TrustManager[] trustAllCertificates = new TrustManager[] { 
				new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
						return new java.security.cert.X509Certificate[0]; 
					}
					@SuppressWarnings("unused")
					public void checkClientTrusted(X509Certificate[] certs, String authType) {}
					@SuppressWarnings("unused")
					public void checkServerTrusted(X509Certificate[] certs, String authType) {}
					@Override
					public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {
					}
					@Override
					public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {
					}
				}};
		
		try
		{
			SSLContext sct = SSLContext.getInstance("SSL");
			sct.init(null, trustAllCertificates, new SecureRandom());
			SocketFactory socketFactory = sct.getSocketFactory();
			
			if(socket == null)
			{
				socket = (SSLSocket) socketFactory.createSocket(hostName, portNumber);
				session = socket.getSession();
			}
			out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			out.println(command);
			out.println();
			out.flush();
			in = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			sBuilder = new StringBuilder();
			Long startTime = System.currentTimeMillis();
			Long endTime = startTime + 3200;
			
			while (System.currentTimeMillis() < endTime) 
			{

				line  = in.readLine();
				if(line !=null && !line.equals("null") && !line.equals(""))
					sBuilder.append(line);
			}
			
			result = sBuilder.toString();
			result.replaceAll("null", "");
//			if(result == null || result.equals(""))
//			{
//				result = "Bad Response / No Response";
//			}
			
		}
		catch(Exception e)
		{
			isException = true;
			Log.e("ExecuteCommand Error", e.toString());
			Log.e("ExecuteCommand Error", e.getMessage());
			result = e.getMessage();
		}
		finally
		{
			try {
				if(socket != null){
					socket.close();
				}
				if(out != null){
					out.close();
				}
				if(in != null){
					in.close();
				}
				
				session = null;
				sBuilder = null;
				
			} catch (IOException e) {
				isException = true;
				e.printStackTrace();
				result = e.getMessage();
			}
			
		}
		
		
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
			callBack.commandExecutionEnd(result, isException);
     }

}
