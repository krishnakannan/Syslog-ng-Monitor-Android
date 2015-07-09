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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class TempAddInstanceIntoDb {
	
	public Boolean status = false;
	private Context context;
	private String instanceName;
	private String hostName;
	private String portNumber;
	
	public TempAddInstanceIntoDb(){
		/*
		 * Empty Constructor Modify when Needed 
		 */
	}
	
	public TempAddInstanceIntoDb(Context context, String instanceName, String hostName, String portNumber){
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
