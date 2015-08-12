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
import android.app.FragmentTransaction;
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

public class SyslogngFragment extends Fragment {
    public static final String ACTIONBAR_TITLE = "menu_title";
    public Boolean changeStatus = false;
    
    private Context context;
	private Spinner spinnerClientCertificate;
	private CheckBox cbIncludeCertificate;
	private List<String> fileList = new ArrayList<String>();
	private EditText editTextHostName;
	private EditText editTextPortNumber;
	private EditText etCertificatePasswordText;
	private EditText editTextInstanceName;
	private Boolean isClientCertificateUsed = false;
	private Syslogng syslogng;
	
    private ArrayAdapter<String> certAdapter;
    
    
    public SyslogngFragment() {
        // Empty constructor required for fragment subclasses
    }
    
    public SyslogngFragment(Context context, Syslogng syslogng){
    	this.context = context;
    	if(syslogng != null){
    		this.syslogng = syslogng;
    	}
    	else{
    		this.syslogng = new Syslogng();
    	}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_syslogng, container, false);
        int i = getArguments().getInt(ACTIONBAR_TITLE);
        String actionbarTitle = getResources().getStringArray(R.array.menu_array)[i];
        FileManager fManager = new FileManager(context);
        File certificateDir = new File(fManager.getCertificateDirectory());
        
        getCertificateNames(certificateDir);
        
        spinnerClientCertificate = (Spinner) rootView.findViewById(R.id.ai_spinner_certificate);
        certAdapter = new ArrayAdapter<String>(getActivity(),
		        android.R.layout.simple_spinner_item, fileList);
        certAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClientCertificate.setAdapter(certAdapter);
        
        etCertificatePasswordText = (EditText) rootView.findViewById(R.id.ai_et_certificate_pass);
        
        spinnerClientCertificate.setVisibility(View.INVISIBLE);
        etCertificatePasswordText.setVisibility(View.INVISIBLE);
        
        
        cbIncludeCertificate = (CheckBox) rootView.findViewById(R.id.ai_cb_include_certificate);
        
		
		cbIncludeCertificate.setEnabled(!fileList.isEmpty());
		
		
		
        editTextInstanceName = (EditText) rootView.findViewById(R.id.ai_et_syslogng_name);
        editTextHostName = (EditText) rootView.findViewById(R.id.ai_et_syslogng_input);
        editTextPortNumber = (EditText) rootView.findViewById(R.id.ai_et_port_input);
        
        populateAllValues();
        
        
        Button btnImportCertificate = (Button) rootView.findViewById(R.id.ai_btn_import_certificate);
        btnImportCertificate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				btnImportCertificateOnClick();
			}
		});
        
        Button btnAddSyslogng = (Button) rootView.findViewById(R.id.ai_btn_save_syslogng);
        btnAddSyslogng.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				btnAddSyslogngOnClick();
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
				
				syslogng.setCertificateFileName(parent.getItemAtPosition(position).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
		});
        
        
        
        
        getActivity().setTitle(actionbarTitle);
        return rootView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        

    }
    
    private void insertSyslogng(){
    	
    	Syslogng syslogng = this.syslogng;
    	
    	
    		
    	SQLiteManager sManager = new SQLiteManager(getActivity().getApplicationContext());
    	if(isClientCertificateUsed){
            changeStatus = sManager.insertSyslogng(syslogng);
    	}
    	else{
    		syslogng.setCertificateFileName("");
    		changeStatus = sManager.insertSyslogng(syslogng);
    	}
    	
        
        if(changeStatus){
        	
        	
        	Toast.makeText(getActivity().getApplicationContext(), context.getString(R.string.insert_instance_success) , Toast.LENGTH_LONG).show();
        	
        }
        else{
        	Toast.makeText(getActivity().getApplicationContext(), context.getString(R.string.insert_instance_failure) , Toast.LENGTH_LONG).show();
        }
    }
    
    private void updateSyslogng(){
    	
    	Syslogng syslogng = this.syslogng;
    	
    	
    	if(this.syslogng.getKey() != null){
    		syslogng.setKey(this.syslogng.getKey());
    	}
    	SQLiteManager sManager = new SQLiteManager(getActivity().getApplicationContext());
    	if(isClientCertificateUsed){
            changeStatus = sManager.updateSyslogng(syslogng); 
    	}
    	else{
    		syslogng.setCertificateFileName("");
    		changeStatus = sManager.updateSyslogng(syslogng);
    	}
    	
        
        if(changeStatus){
        	
        	
        	Toast.makeText(getActivity().getApplicationContext(), context.getString(R.string.insert_instance_success) , Toast.LENGTH_LONG).show();
        	
        }
        else{
        	Toast.makeText(getActivity().getApplicationContext(), context.getString(R.string.insert_instance_failure) , Toast.LENGTH_LONG).show();
        }
    }
    
    private void populateAllValues(){
    	    	
    	if(syslogng != null && syslogng.getKey() != null){
    		
    	
    		editTextInstanceName.setText(syslogng.getSyslogngName());
    		editTextHostName.setText(syslogng.getHostName());
    		editTextPortNumber.setText(syslogng.getPortNumber());
    	
    		spinnerClientCertificate.setVisibility(View.VISIBLE);
        	etCertificatePasswordText.setVisibility(View.VISIBLE);
    	
    		if(syslogng.getCertificateFileName() != null && !syslogng.getCertificateFileName().equals("")){
    			isClientCertificateUsed = true;
    			cbIncludeCertificate.setChecked(true);
    			spinnerClientCertificate.setSelection(certAdapter.getPosition("certificateFileName"));
    			etCertificatePasswordText.setText(syslogng.getCertificatePassword());
    		}
    	}
    }
    
    private Boolean checkHostValidity(String instanceString){
		
		return (instanceString != null && !instanceString.equals(""));
	}
	
    private Boolean checkInstanceNameValidity(String instanceName){
		
		return (instanceName != null && !instanceName.equals(""));
	}
	
	private Boolean checkPortValidity(String portString){
		
		return (portString != null && !portString.equals(""));
	}
	
	private void getCertificateNames(File certificateDir){
		File[] certificates = certificateDir.listFiles();
        for(File file : certificates){
        	fileList.add(file.getName());
        }
	}
	
	private void btnImportCertificateOnClick(){
		Bundle args = new Bundle();
    	Fragment fragment = new ImportCertificateFragment(context);
		args.putInt(SyslogngFragment.ACTIONBAR_TITLE, 1);
		fragment.setArguments(args);
		FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
		transaction.replace(R.id.container, fragment, "fragment_importcert_tag").commit();
		MainActivity.updateDrawer(1);
	}
	
	private void btnAddSyslogngOnClick(){
		
		Integer tempPortNumber;
        
        syslogng.setSyslogngName(editTextInstanceName.getText().toString());
        syslogng.setHostName(editTextHostName.getText().toString());
        syslogng.setPortNumber(editTextPortNumber.getText().toString());
        
        if(checkInstanceNameValidity(syslogng.getSyslogngName()) && checkHostValidity(syslogng.getHostName()) && checkPortValidity(syslogng.getPortNumber())){
        	
        	try{
        		syslogng.setCertificatePassword(etCertificatePasswordText.getText().toString());
        		tempPortNumber = Integer.parseInt(syslogng.getPortNumber()); // remove tempPortNumber
        		if(syslogng.getKey() != null){
        			updateSyslogng();
        		}
        		else{
        			insertSyslogng();
        		}
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
	
}
