package com.mobile.syslogng.monitor;

public class SQLiteConstants {

	static final String DATABASENAME = "instances.db";
	
	static final Integer DATABASEVERSION = 1;
	
	static final String CREATEINSTANCETABLE = "CREATE TABLE if not exists INSTANCE_TABLE(_id INTEGER PRIMARY KEY, INSTANCE_NAME TEXT, INSTANCE_HOSTNAME TEXT, PORT_NUMBER TEXT, CERT_PATH TEXT, CERT_PASSWORD TEXT)";
	
	static final String SELECTALLINSTANCES = "select * from INSTANCE_TABLE";
	
	static final String INSERTINSTANCE = "INSERT INTO INSTANCE_TABLE(INSTANCE_NAME,INSTANCE_HOSTNAME,PORT_NUMBER,CERT_PATH,CERT_PASSWORD) VALUES(?, ?, ?, ?, ?)";
	
	static final String DELETEINSTANCES = "DELETE FROM INSTANCE_TABLE WHERE _id = ?";
}
