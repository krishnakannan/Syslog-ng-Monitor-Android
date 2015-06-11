package com.mobile.syslogng.monitor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class AddInstanceIntoDb {
	
	public Boolean status = false;
	private Context context;
	private String instanceName;
	private String hostName;
	private String portNumber;
	
	public AddInstanceIntoDb(){
		/*
		 * Empty Constructor Modify when Needed 
		 */
	}
	
	public AddInstanceIntoDb(Context context, String instanceName, String hostName, String portNumber){
		this.context = context;
		this.instanceName = instanceName;
		this.hostName = hostName;
		this.portNumber = portNumber;
	}
	
	public Boolean insert()
	{
		String query;
		SQLiteDatabase instanceDb = context.openOrCreateDatabase("instances.db",SQLiteDatabase.CREATE_IF_NECESSARY, null);
		
		query = "INSERT INTO INSTANCE_TABLE(INSTANCE_NAME,INSTANCE_HOSTNAME,PORT_NUMBER) VALUES('"+instanceName+"','"+hostName+"','"+portNumber+"')";
		
		try{
			instanceDb.execSQL(query);
			status = true;
		}
		catch(Exception e){
			status = false;
		}
		finally{
			instanceDb.close();
		}
		return status;
	}
	
	
	

}
