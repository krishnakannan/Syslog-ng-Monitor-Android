package com.mobile.syslogng.monitor;



import java.util.ArrayList;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


public class SQLiteManager extends SQLiteOpenHelper{
	
	
static final String DATABASENAME = "instances.db";
	
	static final Integer DATABASEVERSION = 1;
	
	static final String CREATETABLE = "CREATE TABLE if not exists INSTANCE_TABLE(_id INTEGER PRIMARY KEY, INSTANCE_NAME TEXT, INSTANCE_HOSTNAME TEXT, PORT_NUMBER TEXT, CERT_PATH TEXT, CERT_PASSWORD TEXT)";
	
	static final String SELECTALL = "SELECT * FROM INSTANCE_TABLE";
	
	static final String INSERT = "INSERT INTO INSTANCE_TABLE(INSTANCE_NAME,INSTANCE_HOSTNAME,PORT_NUMBER,CERT_PATH,CERT_PASSWORD) VALUES(?, ?, ?, ?, ?)";
	
	static final String DELETE = "DELETE FROM INSTANCE_TABLE WHERE _id = ?";
	
	static final String SELECT = "SELECT * FROM INSTANCE_TABLE WHERE _id = ?";
	
	static final String UPDATE = "UPDATE INSTANCE_TABLE SET INSTANCE_NAME = ?,INSTANCE_HOSTNAME = ?, PORT_NUMBER = ?, CERT_PATH = ?, CERT_PASSWORD = ? WHERE _id = ?";
	
	Context context;
	
	public SQLiteManager(Context context) {
		super(context, DATABASENAME, null, DATABASEVERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATETABLE);
		
		Log.i("CREATE DB", "in onCreate Method");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	public Boolean insertSyslogng(Syslogng syslogng)
	{
		Boolean status = true;
		SQLiteDatabase db = getWritableDatabase();
			SQLiteStatement insertStatement = db.compileStatement(INSERT);
			insertStatement.bindString(1, syslogng.getSyslogngName());
			insertStatement.bindString(2, syslogng.getHostName());
			insertStatement.bindString(3, syslogng.getPortNumber());
			if(syslogng.getCertificateFileName() != null){
				insertStatement.bindString(4, syslogng.getCertificateFileName());
			}
			if(syslogng.getCertificatePassword() != null){
				insertStatement.bindString(5, syslogng.getCertificatePassword());
			}
			
			
			try{
				insertStatement.executeInsert();
			}
			catch(SQLException e){
				status = false;
			}
			finally{
				insertStatement.close();
				db.close();
			}	
			
		return status;
	}
	
	public Boolean updateSyslogng(Syslogng syslogng){
		Boolean status = true;
		SQLiteDatabase db = getWritableDatabase();
		
		SQLiteStatement updateStatement = db.compileStatement(UPDATE);
		updateStatement.bindString(1, syslogng.getSyslogngName());
		updateStatement.bindString(2, syslogng.getHostName());
		updateStatement.bindString(3, syslogng.getPortNumber());
		if(syslogng.getCertificateFileName() != null){
			updateStatement.bindString(4, syslogng.getCertificateFileName());
		}
		if(syslogng.getCertificatePassword() != null){
			updateStatement.bindString(5, syslogng.getCertificatePassword());
		}
		updateStatement.bindString(6, Integer.toString(syslogng.getKey()));
		try{
			updateStatement.executeUpdateDelete();
		}
		catch(SQLException e){
			status = false;
		}
		finally{
			updateStatement.close();
			db.close();
		}
		
		
		return status;
	}
	
	public ArrayList<Syslogng> getSyslogngs()
	{
		SQLiteDatabase db = getReadableDatabase();
		ArrayList<Syslogng> list = new ArrayList<Syslogng>();
		
		Cursor cursor = db.rawQuery(SELECTALL, null);
		
		while (cursor.moveToNext()) {
			Syslogng syslogng = new Syslogng();
			syslogng.setKey(cursor.getInt(0));
			syslogng.setSyslogngName(cursor.getString(1));
			syslogng.setHostName(cursor.getString(2));
			syslogng.setPortNumber(cursor.getString(3));
			syslogng.setCertificateFileName(cursor.getString(4));
			syslogng.setCertificatePassword(cursor.getString(5));
			list.add(syslogng);
		}
		
		cursor.close();
		db.close();
		
		return list;
	}

	public Boolean deleteSyslogngs(ArrayList<Integer> itemsToDelete){
		
		Boolean status = false;
		SQLiteDatabase db = getWritableDatabase();
		SQLiteStatement deleteStatement = db.compileStatement(DELETE);
		try{
			
		for(Integer itemKey : itemsToDelete){
			deleteStatement.bindString(1, Integer.toString(itemKey));
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
