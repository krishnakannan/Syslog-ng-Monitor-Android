package com.mobile.syslogng.monitor;

import android.content.Context;
import android.os.AsyncTask;

public class ExecuteCommandTask extends AsyncTask<String, Void, String>{

	private Context context;
	private String hostName;
	private Integer portNumber;
	private String command;
	private IExecuteCommandCallBack callBack;
	
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
		
		
		
		return null;
	}

}
