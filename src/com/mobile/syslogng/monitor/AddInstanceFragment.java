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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddInstanceFragment extends Fragment {
    public static final String ACTIONBAR_TITLE = "menu_title";
    public Boolean insertStatus = false;
    
    private Context context;
	private Spinner spinnerClientCertificate;
	private List<String> fileList = new ArrayList<String>();
	private EditText etInstanceText;
	private EditText etPortText;
	private EditText etCertificatePasswordText;
	private EditText etInstanceName;
	private Boolean isClientCertificateUsed = false;
	private String certificateFileName;
    
    public AddInstanceFragment() {
        // Empty constructor required for fragment subclasses
    }
    
    public AddInstanceFragment(Context context){
    	this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_instance, container, false);
        int i = getArguments().getInt(ACTIONBAR_TITLE);
        String actionbarTitle = getResources().getStringArray(R.array.menu_array)[i];
        FileManager fManager = new FileManager(context);
        File certificateDir = new File(fManager.getCertificateDirectory());
        
        File[] certificates = certificateDir.listFiles();
        for(File file : certificates){
        	fileList.add(file.getName());
        }
        
        spinnerClientCertificate = (Spinner) rootView.findViewById(R.id.ai_spinner_certificate);
        ArrayAdapter<String> certAdapter = new ArrayAdapter<String>(getActivity(),
		        android.R.layout.simple_spinner_item, fileList);
        certAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClientCertificate.setAdapter(certAdapter);
        
        etCertificatePasswordText = (EditText) rootView.findViewById(R.id.ai_et_certificate_pass);
        
        spinnerClientCertificate.setVisibility(View.INVISIBLE);
        etCertificatePasswordText.setVisibility(View.INVISIBLE);
        
       
        
        
        getActivity().setTitle(actionbarTitle);
        return rootView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        CheckBox cbIncludeCertificate = (CheckBox) getActivity().findViewById(R.id.ai_cb_include_certificate);
		if(fileList.isEmpty()){
			cbIncludeCertificate.setEnabled(false);
		}
		else{
			cbIncludeCertificate.setEnabled(true);
		}
        
        final EditText editTextInstanceName = (EditText) getActivity().findViewById(R.id.ai_et_instance_name);
        final EditText editTextHostName = (EditText) getActivity().findViewById(R.id.ai_et_instance_input);
        final EditText editTextPortNumber = (EditText) getActivity().findViewById(R.id.ai_et_port_input);
        
        
        Button btnAddInstance = (Button) getActivity().findViewById(R.id.ai_btn_save_instance);
        btnAddInstance.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				String instanceName;
		        String hostName;
		        String portNumber;
		        String certificatePassword = "";
		        Integer tempPortNumber;
		        
		        instanceName = editTextInstanceName.getText().toString();
		        hostName = editTextHostName.getText().toString();
		        portNumber = editTextPortNumber.getText().toString();
		        
		        if(checkInstanceNameValidity(instanceName) && checkHostValidity(hostName) && checkPortValidity(portNumber)){
		        	
		        	try{
		        		certificatePassword = etCertificatePasswordText.getText().toString();
		        		tempPortNumber = Integer.parseInt(portNumber);
		        		insertInstance(instanceName, hostName, portNumber, certificateFileName, certificatePassword);
		        		editTextInstanceName.setText("");
		            	editTextHostName.setText("");
		            	editTextPortNumber.setText("");
		            	etCertificatePasswordText.setText("");
		        	}
		        	catch(NumberFormatException e){
		        		Toast.makeText(getActivity().getApplicationContext(), context.getString(R.string.port_error), Toast.LENGTH_LONG).show();
		        	}
		        	
		        }
		        else{
		        	Toast.makeText(getActivity().getApplicationContext(), context.getString(R.string.host_port_error), Toast.LENGTH_LONG).show();
		        }
		        
		        
		        	
			}
        });
        
        cbIncludeCertificate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					spinnerClientCertificate.setVisibility(View.VISIBLE);
			        etCertificatePasswordText.setVisibility(View.VISIBLE);
			        isClientCertificateUsed = true;
				}
				else{
					spinnerClientCertificate.setVisibility(View.INVISIBLE);
			        etCertificatePasswordText.setVisibility(View.INVISIBLE);
			        isClientCertificateUsed = false;
				}
				
			}
			
		});
        
        spinnerClientCertificate.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				
				certificateFileName = parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
		});

    }
    
    public void insertInstance(String instanceName, String hostName, String portNumber, String certPath, String certPassword){
    	
    	SQLiteManager sManager = new SQLiteManager(getActivity().getApplicationContext());
    	if(isClientCertificateUsed){
            insertStatus = sManager.insertInstance(instanceName, hostName, portNumber, certPath, certPassword);
    	}
    	else{
    		certPath = "";
    		insertStatus = sManager.insertInstance(instanceName, hostName, portNumber, certPath, certPassword);
    	}
    	
        
        if(insertStatus){
        	
        	
        	Toast.makeText(getActivity().getApplicationContext(), context.getString(R.string.insert_instance_success) , Toast.LENGTH_LONG).show();
        	
        }
        else{
        	Toast.makeText(getActivity().getApplicationContext(), context.getString(R.string.insert_instance_failure) , Toast.LENGTH_LONG).show();
        }
    }
    
    public Boolean checkHostValidity(String instanceString){
		
		return (instanceString != null && !instanceString.equals(""));
	}
	
    public Boolean checkInstanceNameValidity(String instanceName){
		
		return (instanceName != null && !instanceName.equals(""));
	}
	
	public Boolean checkPortValidity(String portString){
		
		return (portString != null && !portString.equals(""));
	}
}
