package com.mobile.syslogng.monitor;

public interface IExecuteCommandCallBack{

	void commandExecutionStart();
	void commandExecutionEnd(String result, Boolean Exception);
	
}
