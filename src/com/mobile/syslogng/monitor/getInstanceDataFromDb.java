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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class getInstanceDataFromDb extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "instances.db";

    private static final int DATABASE_VERSION = 1;
    
    public getInstanceDataFromDb(Context context)
	{
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	
	public ArrayList<HashMap<String,String>> getInstancesData()
	{
		SQLiteDatabase db = getReadableDatabase();
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Cursor cursor = db.rawQuery("select * from INSTANCE_TABLE", null);
		
		while (cursor.moveToNext()) {
			HashMap<String, String> tempMap = new HashMap<String, String>();
			tempMap.put("InstanceName", cursor.getString(1));
			tempMap.put("HostName", "Hostname :"+cursor.getString(2));
			tempMap.put("PortNumber", "Port :"+cursor.getString(3));
			list.add(tempMap);
		}
		
		cursor.close();
		db.close();
		
		return list;
	}
	
}
