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
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class TempConnector extends Activity {

	SSLSocket socket;
	SSLSession session;
	EstablishConn eConn;
	String setCommand = null;
	String line = null;
	Thread thread;
	PrintWriter out;
	String reply = null;
	StringBuilder sBuilder = null;
	ProgressBar pBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temp_connector);
		
		pBar = (ProgressBar) findViewById(R.id.progressBar1);
		String hostName = null;
		Integer portNumber = null;
		pBar.setVisibility(View.INVISIBLE);
		
		getActionBar().setDisplayShowTitleEnabled(false);
		
		//Get the value from the previous Activity
		Bundle extras = getIntent().getExtras();
		hostName = extras.getString("Instance");
		portNumber = extras.getInt("Port");
		
		
		eConn = new EstablishConn(hostName, portNumber);
		eConn.execute(null,null,null);
		
		
		Button statsBtn = (Button) findViewById(R.id.stats);
		Button isAliveBtn = (Button) findViewById(R.id.is_alive);
		Button showConfigBtn = (Button) findViewById(R.id.show_config);
		
		statsBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setCommand = "stats";
				pBar.setVisibility(View.VISIBLE);
				execCommand(setCommand);
			}
		});
		
		isAliveBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setCommand = "is_alive";
				pBar.setVisibility(View.VISIBLE);
				execCommand(setCommand);
			}
		});
		
		showConfigBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setCommand = "show_config";
				pBar.setVisibility(View.VISIBLE);
				execCommand(setCommand);
			}
		});
	
		Button closeBtn = (Button) findViewById(R.id.closeWindowButton);
		closeBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
	}
	
	protected void execCommand(final String command)
	{
		final TextView msgTextView = (TextView) findViewById(R.id.msgTextView);
		
		thread = new Thread(new Runnable() {
			public void run() {
				BufferedReader in = null;
				try
				{
					reply = "";

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							pBar.setVisibility(View.VISIBLE);
							msgTextView.setText("");
						}
					});   

					out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
					out.println(command);
					out.println();
					out.flush();
					in = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));
					sBuilder = new StringBuilder();
					long startTime = System.currentTimeMillis();
					long waitTime = 3000;
					long endTime = startTime + waitTime;
					while (System.currentTimeMillis() < endTime && reply != "Bad Response / No Response") 
					{

						line  = in.readLine();
						if(line != "null" && line !=null)
							sBuilder.append(line);
						reply = sBuilder.toString();
						reply.replaceAll("null", "");
						if(reply == null || reply == "")
						{
							reply = "Bad Response / No Response";
						}
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								msgTextView.setText(reply);
								pBar.setVisibility(View.INVISIBLE);
							}
						});

					}




				}
				catch(final Exception e)
				{
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							msgTextView.setText("Exception "+e.getMessage());
							pBar.setVisibility(View.INVISIBLE);
						}
					});
				}
				finally
				{
					try 
					{
						in.close();
					} 
					catch (final IOException e) 
					{
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								msgTextView.setText(e.toString());
								pBar.setVisibility(View.INVISIBLE);
							}
						});
					}
					out.close();
				}

			}
		});
		thread.start();
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		eConn = null;

		if(socket != null)
		{
			if(thread != null)
				thread.interrupt();
			thread = null;
			if(out != null)
				out.close();
			CloseConnection cc = new CloseConnection();
			cc.execute(null,null,null);
			cc = null;   
		}

	}
	
	@SuppressLint("TrulyRandom")
	class EstablishConn extends AsyncTask<String, Void, String>
	{
		String iURL = null;
		Integer iPortNumber;

		public EstablishConn(String instanceURL, Integer portNumber)
		{
			this.iURL = instanceURL;
			this.iPortNumber = portNumber;
		}
		
		public EstablishConn(String hostName, Integer portNumber, Integer certType, String suite)
		{
			// For future Usage
		}
		
		@SuppressLint("TrulyRandom")
		@Override
		protected String doInBackground(String... params) {
			
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
					socket = (SSLSocket) socketFactory.createSocket(iURL, iPortNumber);
					session = socket.getSession();
				}
			}
			catch(Exception e)
			{
				Log.e("CreateErr", e.toString());
				Log.e("CreateErr", e.getMessage());
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast toast = Toast.makeText(getApplicationContext(), "Error in Socket Creation, Please check the URL", Toast.LENGTH_LONG);
						toast.show();
					}
				});
			}
			finally
			{
				
			}
			
			
			return null;
		}
		
	}

	class CloseConnection extends AsyncTask<String,Void,String>
	{

		@Override
		protected String doInBackground(String... params) {
			try 
			{
				socket.close();
				session = null;
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			return null;
		}

	}
}


