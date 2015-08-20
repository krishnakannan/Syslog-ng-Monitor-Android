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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AbsListView.MultiChoiceModeListener;


public class MonitoredSyslogngFragment extends Fragment implements ICommandCallBack{

	
	private String command;
	private Map<Integer,Integer> itemsDisplayed = new LinkedHashMap<Integer, Integer>();
	private HashSet<Integer> itemsSelected = new HashSet<Integer>();
	private Context context;
	private IMainActivity mainActivityCallBack;
	
	ListView listViewSyslogngs;
	
	
	
	
	public MonitoredSyslogngFragment(IMainActivity mainActivityCallBack, Context context){
		this.context = context;
		this.mainActivityCallBack = mainActivityCallBack;
	}
	
    public MonitoredSyslogngFragment() {
        // Empty constructor required for fragment subclasses
    }

    ArrayList<Syslogng> syslogngs = new ArrayList<Syslogng>();
    
    SQLiteManager sManager;
    
    @Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	
    	
    	sManager = new SQLiteManager(context);
        syslogngs = sManager.getSyslogngs();
    	
        
        /*
    	 * Storing the items displayed in the List - Position and Primary Key.
    	 * Position starting from 0 and incremented. 
    	 * Position and Corresponding record's primary key is stored as key,value.
    	 *  
    	 */
        Integer iterator = 0;
        for(Syslogng syslogng : syslogngs){
        	itemsDisplayed.put(iterator++, syslogng.getKey());
        }
        
    	View rootView = inflater.inflate(R.layout.fragment_monitored_syslogng, container, false);
        
    	listViewSyslogngs	 = (ListView) rootView.findViewById(R.id.listview_view_instance);
    	listViewSyslogngs.setAdapter(getListViewAdapter(syslogngs));
    	
    	
    	
    	
    	
    	int i = getArguments().getInt(MainActivity.FRAGMENT_POS);
        String actionbarTitle = getResources().getStringArray(R.array.menu_array)[i];
        
        getActivity().setTitle(actionbarTitle);
        
        
        
        this.listViewSyslogngs.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        this.listViewSyslogngs.setMultiChoiceModeListener(new MultiChoiceModeListener() {
        	
        	@Override
        	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        		MenuInflater inflater = mode.getMenuInflater();
        	        inflater.inflate(R.menu.context_menu, menu);
        	       
        	        return true;
        	}


        	@Override
        	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        		// TODO Auto-generated method stub
        		return false;
        	}


        	@Override
        	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        		//Responds to clicked item.
        		switch (item.getItemId()) {
        		
        			case R.id.delete_list_item:
        				if(deleteSyslogngs()){
        					reloadListView();
        					mode.finish();
        					Toast.makeText(context, context.getString(R.string.delete_success), Toast.LENGTH_LONG).show();
        				}
        				else{
        					Toast.makeText(context, context.getString(R.string.delete_failure), Toast.LENGTH_LONG).show();
        				}
        				
        				break;
        			case R.id.edit_list_item:
        				if(itemsSelected.size() == 1){
        					loadSyslogngFragment(syslogngs.get(itemsSelected.iterator().next()));
        					mode.finish();
        				}
        				else{
        					Toast.makeText(context, context.getString(R.string.edit_validation_failure), Toast.LENGTH_LONG).show();
        				}
        				break;
        		
        		}
        		
        		return false;
        	}


        	@Override
        	public void onDestroyActionMode(ActionMode mode) {
        		// TODO Auto-generated method stub
        		listViewSyslogngs.setActivated(false);
        	}


        	
        	@Override
        	public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        		if(checked == true){
        			
        			itemsSelected.add(position);
        			listViewSyslogngs.getChildAt(position).setActivated(true);

        		}
        		else if(checked == false){
        			itemsSelected.remove(position);
        			listViewSyslogngs.getChildAt(position).setActivated(false);
        			
        		}
        		
        	}
        	
        });
        
        this.listViewSyslogngs.setOnItemClickListener(new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

			
			final List<String> commandList = Arrays.asList(context.getResources().getStringArray(R.array.command_array));
			
			
			AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
			dialog.setTitle(R.string.select_command);
		    
			dialog.setSingleChoiceItems(R.array.command_array, -1, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					command = commandList.get(which);					
				}
			});
			
			dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	executeCommandTask(syslogngs.get(position), command);
		        }
		     });
		    dialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			
		    
		     
		    dialog.setIconAttribute(android.R.attr.alertDialogIcon);
		    dialog.show();
			
		}
    	   
       });
        
        return rootView;
    }
    
    private Boolean deleteSyslogngs(){
    	ArrayList<Integer> itemsToDelete = new ArrayList<Integer>();
    	for(Integer item : itemsSelected){
    		itemsToDelete.add(itemsDisplayed.get(item)); 
    	}
    	itemsSelected.clear();
    	return sManager.deleteSyslogngs(itemsToDelete);
    }
    
    private SimpleAdapter getListViewAdapter(ArrayList<Syslogng> list){
    	ArrayList<HashMap<String,String>> dataList = new ArrayList<HashMap<String, String>>();
    	for(Syslogng items : list){
    		 HashMap<String,String> dataListItem = new HashMap<String,String>();
    		 dataListItem.put("SyslogngName", items.getSyslogngName());
    		 dataListItem.put("HostName", items.getHostName());
    		 dataListItem.put("PortNumber", items.getPortNumber());
    		 dataList.add(dataListItem);
    	}
    	return new SimpleAdapter(getActivity().getBaseContext(), dataList, R.layout.rows_design, 
    			new String[] { "SyslogngName", "HostName", "PortNumber"}, 
    			new int[] { R.id.textview_instance_name, R.id.textview_hostname, R.id.textview_portnumber });
    }
    
    
    
    private void reloadListView(){
    	syslogngs = sManager.getSyslogngs();
    	Integer iterator = 0;
    	itemsDisplayed.clear();
        for(Syslogng syslogng : syslogngs){
        	itemsDisplayed.put(iterator++, syslogng.getKey());
        }
    	listViewSyslogngs.setAdapter(getListViewAdapter(sManager.getSyslogngs()));
    	listViewSyslogngs.invalidate();
    }
    
    private void loadSyslogngFragment(Syslogng syslogng){
    	
    		Log.i("instanceData", syslogng.getHostName()+" "+ syslogng.getPortNumber() +" "+ syslogng.getKey());
    	
    	itemsSelected.clear();
    	mainActivityCallBack.setFragment(new SyslogngFragment(mainActivityCallBack,context, syslogng), MainActivity.SYSLOGNG_FRAGMENT_POS, "fragment_addsyslogng_tag");
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
    }

	
    
    
    private void executeCommandTask(Syslogng syslogng, String command){
    	
    		CommandTask commandTask = new CommandTask(this, getActivity(), syslogng, command);
			commandTask.execute();
    	
    }
    
    @Override
	public void commandExecutionStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commandExecutionEnd(String result, Boolean isException) {
		showResult(result, isException);	
	}

	private void showResult(String message, Boolean isException){
		
		if(isException){
			new AlertDialog.Builder(getActivity())
		    .setTitle(R.string.error)
		    .setMessage(message)
		    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            
		        }
		     }).setIconAttribute(android.R.attr.alertDialogIcon)
		     .show();	
		}
		else{
			new AlertDialog.Builder(getActivity())
		    .setTitle(R.string.result)
		    .setMessage(message)
		    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            
		        }
		     }).setIconAttribute(android.R.attr.icon)
		     .show();
		}
		
		
	}

	

}
