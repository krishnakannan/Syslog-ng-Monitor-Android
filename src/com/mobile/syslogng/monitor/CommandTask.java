/*

	This program is free software: you can redistribute it and/or modify

    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
 */


package com.mobile.syslogng.monitor;



import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


@SuppressLint("TrulyRandom")
public class CommandTask extends AsyncTask<String, Void, String>{

	private Context context;
	private String hostName;
	private Integer portNumber;
	private String command;
	private ICommandCallBack callBack;
	private String certificateFileName;
	private String certificatePassword;
	private String result = null;
	
	
	private Boolean isException = false;
	private ProgressDialog progressDialog;
	
	public CommandTask(){
		// Empty Constructor
	}
	

	
	public CommandTask(ICommandCallBack callBack, Context context, String hostName, Integer portNumber, String command, String certificateFileName, String certificatePassword){
		
		this.callBack = callBack;
		this.context = context;
		this.hostName = hostName;
		this.portNumber = portNumber;
		this.command = command;
		this.certificateFileName = certificateFileName;
		this.certificatePassword = certificatePassword;
		
		
	}
	
	@Override
	 protected void onPreExecute() {
	  super.onPreExecute();
	  callBack.commandExecutionStart();
	  progressDialog = new ProgressDialog(context);
	  this.progressDialog.setMessage(context.getString(R.string.command_task_progress_dialog));
	  this.progressDialog.show();
	 }
	
	@Override
	protected String doInBackground(String... params) {
		
		SSLSocket socket = null;
		PrintWriter printWriter = null;
		StringBuilder sBuilder = null;
		BufferedReader bufferedReader = null;
		String line = null;
		
		
		SSLContext sct;
		
		
		
		//Implementing for SELFSIGNED CERTIFICATES - Will be changed in future as per needs
		TrustManager[] trustAllCertificates = getTrustManagers();
		
		try
		{
			/*
			 * Implementation of using client certificate.
			 * if part is executed when client certificate is present
			 * else part is executed when client certificate is not present
			 */
			
			KeyManager[] keyManagers = null;
			sct = SSLContext.getInstance("TLS");
			if(certificateFileName != null){
				keyManagers = getKeyManagers();
			}
			sct.init(keyManagers, trustAllCertificates, new SecureRandom());
			
			
			SocketFactory socketFactory = sct.getSocketFactory();
			
			if(socket == null)
			{
				socket = (SSLSocket) socketFactory.createSocket(hostName, portNumber);
				
			}
			printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			printWriter.println(command);
			
			printWriter.flush();
			bufferedReader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			sBuilder = new StringBuilder();
			
			
			while ((line  = bufferedReader.readLine()) != null) 
			{
				if(!line.equals("null") && !line.equals(""))
					sBuilder.append(line);
			}
			
			
				result = sBuilder.toString();
			
			

		}
		catch(IOException e)
		{
			isException = true;
			Log.e("ExecuteCommand Error", e.getMessage());
			result = e.getMessage();
		} catch (KeyManagementException e) {
			isException = true;
			Log.e("ExecuteCommand Error", e.getMessage());
			result = e.getMessage();
		} catch (NoSuchAlgorithmException e) {
			isException = true;
			Log.e("ExecuteCommand Error", e.getMessage());
			result = e.getMessage();
		} catch (UnrecoverableKeyException e) {
			isException = true;
			Log.e("ExecuteCommand Error", e.getMessage());
			result = e.getMessage();
		} catch (KeyStoreException e) {
			isException = true;
			Log.e("ExecuteCommand Error", e.getMessage());
			result = e.getMessage();
		} catch (CertificateException e) {
			isException = true;
			Log.e("ExecuteCommand Error", e.getMessage());
			result = e.getMessage();
		}
		finally
		{
			try {
				if(printWriter != null){
					printWriter.close();
				}
				if(bufferedReader != null){
					bufferedReader.close();
				}
				if(socket != null){
					socket.close();
				}
				
				
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
			progressDialog.dismiss();
			callBack.commandExecutionEnd(result, isException);
     }
	
	public KeyManager[] getKeyManagers() throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException, IOException{
	
		FileManager fManager = new FileManager(context);
		KeyManager[] keyManagers = null;
	
		KeyStore keyStore;
		keyStore = KeyStore.getInstance("PKCS12");
		FileInputStream certificateFileInputStream = new FileInputStream(fManager.getCertificateFile(certificateFileName));
		keyStore.load(certificateFileInputStream, certificatePassword.toCharArray());
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
		keyManagerFactory.init(keyStore, certificatePassword.toCharArray());
		keyManagers = keyManagerFactory.getKeyManagers();
		return keyManagers;
	}
	
	public TrustManager[] getTrustManagers(){
		return  new TrustManager[] { 
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
		
	}

}
