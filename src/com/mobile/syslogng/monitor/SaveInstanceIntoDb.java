package com.mobile.syslogng.monitor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SaveInstanceIntoDb {
	
	public Boolean status = false;
	private Context context;
	private String instanceName;
	private String hostName;
	private Integer portNumber;
	
	public SaveInstanceIntoDb(){
		/*
		 * Empty Constructor Modify when Needed 
		 */
	}
	
	public SaveInstanceIntoDb(Context context, String instanceName, String hostName, Integer portNumber){
		this.context = context;
		this.instanceName = instanceName;
		this.hostName = hostName;
		this.portNumber = portNumber;
	}
	
	public Boolean insert()
	{
		String query;
		SQLiteDatabase instanceDb = context.openOrCreateDatabase("instances.db",SQLiteDatabase.CREATE_IF_NECESSARY, null);
		
		query = "INSERT INTO INSTANCE_TABLE VALUES('"+instanceName+"','"+hostName+"','"+portNumber+"')";
		
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
