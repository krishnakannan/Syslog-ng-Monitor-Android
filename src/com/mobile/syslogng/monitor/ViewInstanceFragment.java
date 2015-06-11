package com.mobile.syslogng.monitor;



import java.util.ArrayList;
import java.util.HashMap;






import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ViewInstanceFragment extends Fragment {

	public static final String ACTIONBAR_TITLE = "menu_title";

	private Context context;
	
 
	
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
        
    	ListView listViewInstances = (ListView) rootView.findViewById(R.id.listview_view_instance);
    	
    	SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), list, R.layout.rows_design, new String[] { "InstanceName", "HostName", "PortNumber" }, new int[] { R.id.textview_instance_name, R.id.textview_hostname, R.id.textview_portnumber });
    	
    	listViewInstances.setAdapter(adapter);
    	
    	int i = getArguments().getInt(ACTIONBAR_TITLE);
        String actionbarTitle = getResources().getStringArray(R.array.menu_array)[i];
        
        getActivity().setTitle(actionbarTitle);
        return rootView;
    }

}
