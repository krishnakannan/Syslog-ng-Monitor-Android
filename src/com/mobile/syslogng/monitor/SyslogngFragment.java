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
    
    public Boolean changeStatus = false;
    
    private Context context;
	private Spinner spinnerClientCertificate;
	private CheckBox cbIncludeCertificate;
	private List<String> fileList = new ArrayList<String>();
	private EditText editTextHostName;
	private EditText editTextPortNumber;
	private EditText etCertificatePasswordText;
	private EditText editTextSyslogngName;
	private Boolean isClientCertificateUsed = false;
	private Syslogng syslogng;
	private IMainActivity mainActivityCallBack;
	
    private ArrayAdapter<String> certAdapter;
    
    
    public SyslogngFragment() {
        // Empty constructor required for fragment subclasses
    }
    
    public SyslogngFragment(IMainActivity mainActivityCallBack, Context context, Syslogng syslogng){
    	this.mainActivityCallBack = mainActivityCallBack;
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
        int i = getArguments().getInt(MainActivity.FRAGMENT_POS);
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
		
		
		
        editTextSyslogngName = (EditText) rootView.findViewById(R.id.ai_et_syslogng_name);
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
    
    private Syslogng copySyslogng(Syslogng syslogng){
    	Syslogng syslog_ng = new Syslogng();
    	syslog_ng.setCertificateFileName(syslogng.getCertificateFileName());
    	syslog_ng.setCertificatePassword(syslogng.getCertificatePassword());
    	syslog_ng.setHostName(syslogng.getHostName());
    	syslog_ng.setKey(syslogng.getKey());
    	syslog_ng.setPortNumber(syslogng.getPortNumber());
    	syslog_ng.setSyslogngName(syslogng.getSyslogngName());
    	return syslog_ng;
    }
    
    private void insertSyslogng(){
    	
    	Syslogng syslogng = copySyslogng(this.syslogng);
    	
    	
    		
    	SQLiteManager sManager = new SQLiteManager(getActivity().getApplicationContext());
    	if(isClientCertificateUsed){
            changeStatus = sManager.insertSyslogng(syslogng);
    	}
    	else{
    		syslogng.setCertificateFileName("");
    		changeStatus = sManager.insertSyslogng(syslogng);
    	}
    	
        
        if(changeStatus){
        	
        	
        	Toast.makeText(getActivity().getApplicationContext(), context.getString(R.string.insert_syslogng_success) , Toast.LENGTH_LONG).show();
        	
        }
        else{
        	Toast.makeText(getActivity().getApplicationContext(), context.getString(R.string.insert_syslogng_failure) , Toast.LENGTH_LONG).show();
        }
    }
    
    private void updateSyslogng(){
    	
    	Syslogng syslogng = copySyslogng(this.syslogng);
    	
    	
    	SQLiteManager sManager = new SQLiteManager(getActivity().getApplicationContext());
    	if(isClientCertificateUsed){
            changeStatus = sManager.updateSyslogng(syslogng); 
    	}
    	else{
    		syslogng.setCertificateFileName("");
    		changeStatus = sManager.updateSyslogng(syslogng);
    	}
    	
        
        if(changeStatus){
        	
        	
        	Toast.makeText(getActivity().getApplicationContext(), context.getString(R.string.insert_syslogng_success) , Toast.LENGTH_LONG).show();
        	
        }
        else{
        	Toast.makeText(getActivity().getApplicationContext(), context.getString(R.string.insert_syslogng_failure) , Toast.LENGTH_LONG).show();
        }
    }
    
    private void populateAllValues(){
    	    	
    	if(syslogng != null && syslogng.getKey() != null){
    		
    	
    		editTextSyslogngName.setText(syslogng.getSyslogngName());
    		editTextHostName.setText(syslogng.getHostName());
    		editTextPortNumber.setText(syslogng.getPortNumber());
   
    		if(syslogng.getCertificateFileName() != null && !syslogng.getCertificateFileName().equals("")){
    			spinnerClientCertificate.setVisibility(View.VISIBLE);
            	etCertificatePasswordText.setVisibility(View.VISIBLE);
    			isClientCertificateUsed = true;
    			cbIncludeCertificate.setChecked(true);
    			spinnerClientCertificate.setSelection(certAdapter.getPosition("certificateFileName"));
    			etCertificatePasswordText.setText(syslogng.getCertificatePassword());
    		}
    		
    	}
    }
    
    private Boolean checkHostValidity(String syslogngHostName){
		
		return (syslogngHostName != null && !syslogngHostName.equals(""));
	}
	
    private Boolean checkInstanceNameValidity(String syslogngName){
		
		return (syslogngName != null && !syslogngName.equals(""));
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
    	Fragment fragment = new ImportCertificateFragment(mainActivityCallBack, context);
		args.putInt(MainActivity.FRAGMENT_POS, 1);
		fragment.setArguments(args);
		FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
		transaction.replace(R.id.container, fragment, "fragment_importcert_tag").commit();
		mainActivityCallBack.updateDrawer(MainActivity.IMPORT_CERTIFICATE_FRAGMENT_POS);
	}
	
	private void btnAddSyslogngOnClick(){
		
		Integer tempPortNumber;
        
        syslogng.setSyslogngName(editTextSyslogngName.getText().toString());
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
        		editTextSyslogngName.setText("");
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
