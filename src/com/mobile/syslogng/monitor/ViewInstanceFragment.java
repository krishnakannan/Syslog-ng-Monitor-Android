package com.mobile.syslogng.monitor;



import java.util.ArrayList;
import java.util.HashMap;














import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AbsListView.MultiChoiceModeListener;

public class ViewInstanceFragment extends Fragment {

	public static final String ACTIONBAR_TITLE = "menu_title";

	private Context context;
	
	ListView listViewInstances;
	
	public ViewInstanceFragment(Context context){
		this.context = context;
	}
	
	
    public ViewInstanceFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    	
    	getInstanceDataFromDb instanceDataObject = new getInstanceDataFromDb(context);
        list = instanceDataObject.getInstancesData();
    	
    	View rootView = inflater.inflate(R.layout.fragment_view_instance, container, false);
        
    	listViewInstances	 = (ListView) rootView.findViewById(R.id.listview_view_instance);
    	
    	SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), list, R.layout.rows_design, new String[] { "InstanceName", "HostName", "PortNumber" }, new int[] { R.id.textview_instance_name, R.id.textview_hostname, R.id.textview_portnumber });
    	
    	listViewInstances.setAdapter(adapter);
    	
    	
    	
    	int i = getArguments().getInt(ACTIONBAR_TITLE);
        String actionbarTitle = getResources().getStringArray(R.array.menu_array)[i];
        
        getActivity().setTitle(actionbarTitle);
        return rootView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        this.listViewInstances.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        this.listViewInstances.setMultiChoiceModeListener(new MultiChoiceModeListener() {

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
						Toast.makeText(context, "delete", Toast.LENGTH_LONG).show();
						break;
					case R.id.edit_list_item:
						Toast.makeText(context, "edit", Toast.LENGTH_LONG).show();
						break;
				
				}
				
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
				
				if(checked == true)
	 			{
					listViewInstances.getChildAt(position).setActivated(true);
	 				
					listViewInstances.getChildAt(position).setAlpha((float)0.65);
	 			}
				
			}
        	
        });
    }

}
