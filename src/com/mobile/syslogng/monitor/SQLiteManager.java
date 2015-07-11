package com.mobile.syslogng.monitor;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import static com.mobile.syslogng.monitor.SQLiteConstants.DATABASEVERSION;
import static com.mobile.syslogng.monitor.SQLiteConstants.DATABASENAME;
import static com.mobile.syslogng.monitor.SQLiteConstants.INSERTINSTANCE;
import static com.mobile.syslogng.monitor.SQLiteConstants.SELECTALLINSTANCES;
import static com.mobile.syslogng.monitor.SQLiteConstants.DELETEINSTANCES;
import static com.mobile.syslogng.monitor.SQLiteConstants.UPDATEINSTANCE;

public class SQLiteManager extends SQLiteOpenHelper{
	
	Context context;
	
	public SQLiteManager(Context context) {
		super(context, DATABASENAME, null, DATABASEVERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	public Boolean insertUpdateInstance(String instanceName, String hostName, String portNumber, String certPath, String certPassword, Integer key, Boolean isEdit)
	{
		Boolean status;
		SQLiteDatabase instanceDb = context.openOrCreateDatabase(DATABASENAME,SQLiteDatabase.CREATE_IF_NECESSARY, null);
		
		if(isEdit){
			SQLiteStatement updateStatement = instanceDb.compileStatement(UPDATEINSTANCE);
			updateStatement.bindString(1, instanceName);
			updateStatement.bindString(2, hostName);
			updateStatement.bindString(3, portNumber);
			updateStatement.bindString(4, certPath);
			updateStatement.bindString(5, certPassword);
			updateStatement.bindString(6, Integer.toString(key));
			try{
				updateStatement.executeInsert();
				status = true;
			}
			catch(SQLException e){
				status = false;
			}
			finally{
				updateStatement.close();
				instanceDb.close();
			}
		}
		else{
			
			SQLiteStatement insertStatement = instanceDb.compileStatement(INSERTINSTANCE);
			insertStatement.bindString(1, instanceName);
			insertStatement.bindString(2, hostName);
			insertStatement.bindString(3, portNumber);
			insertStatement.bindString(4, certPath);
			insertStatement.bindString(5, certPassword);
			
			try{
				insertStatement.executeInsert();
				status = true;
			}
			catch(SQLException e){
				status = false;
			}
			finally{
				insertStatement.close();
				instanceDb.close();
			}
		}
			
		
		return status;
	}
	
	public ArrayList<HashMap<String,String>> getInstancesData()
	{
		SQLiteDatabase db = getReadableDatabase();
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Cursor cursor = db.rawQuery(SELECTALLINSTANCES, null);
		
		while (cursor.moveToNext()) {
			HashMap<String, String> tempMap = new HashMap<String, String>();
			tempMap.put("Key", Integer.toString(cursor.getInt(0)));
			tempMap.put("InstanceName", cursor.getString(1));
			tempMap.put("HostName", cursor.getString(2));
			tempMap.put("PortNumber",cursor.getString(3));
			tempMap.put("CertPath", cursor.getString(4));
			tempMap.put("CertPass", cursor.getString(5));
			list.add(tempMap);
		}
		
		cursor.close();
		db.close();
		
		return list;
	}

	public Boolean deleteInstancesData(ArrayList<String> itemsToDelete){
		
		Boolean status = false;
		SQLiteDatabase db = getWritableDatabase();
		SQLiteStatement deleteStatement = db.compileStatement(DELETEINSTANCES);
		try{
			
		for(String itemKey : itemsToDelete){
			deleteStatement.bindString(1, itemKey);
			deleteStatement.executeUpdateDelete();
			}
			status = true;
		}
		catch(SQLException e){
			status = false;
		}
		finally{
			deleteStatement.close();
			db.close();
		}
		
		return status;
	}
}
