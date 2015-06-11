package com.mobile.syslogng.monitor;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddInstanceFragment extends Fragment {
    public static final String ACTIONBAR_TITLE = "menu_title";
    public Boolean insertStatus = false;
    public AddInstanceFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_instance, container, false);
        int i = getArguments().getInt(ACTIONBAR_TITLE);
        String actionbarTitle = getResources().getStringArray(R.array.menu_array)[i];
        getActivity().setTitle(actionbarTitle);
        return rootView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        
        
        final EditText editTextInstanceName = (EditText) getActivity().findViewById(R.id.et_instance_name);
        final EditText editTextHostName = (EditText) getActivity().findViewById(R.id.et_host_name);
        final EditText editTextPortNumber = (EditText) getActivity().findViewById(R.id.et_port_number);
        
        Button btnAddInstance = (Button) getActivity().findViewById(R.id.btn_add_instance);
        btnAddInstance.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				String instanceName;
		        String hostName;
		        String portNumber;
		        
		        instanceName = editTextInstanceName.getText().toString();
		        hostName = editTextHostName.getText().toString();
		        portNumber = editTextPortNumber.getText().toString();
		        
		        AddInstanceIntoDb saveInstance = new AddInstanceIntoDb(getActivity().getApplicationContext(),instanceName, hostName, portNumber);
		        insertStatus = saveInstance.insert();
		        
		        if(insertStatus){
		        	
		        	editTextInstanceName.setText("");
		        	editTextHostName.setText("");
		        	editTextPortNumber.setText("");
		        	Toast.makeText(getActivity().getApplicationContext(), "Instance successfully added into Database" , Toast.LENGTH_LONG).show();
		        	
		        }
		        else{
		        	Toast.makeText(getActivity().getApplicationContext(), "There was a problem occured in adding Instance" , Toast.LENGTH_LONG).show();
		        }
		        	
			}
        });
    }
}
