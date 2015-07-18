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

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RunCommandFragment extends Fragment implements ICommandCallBack{
	
	public static final String ACTIONBAR_TITLE = "menu_title";
	
	private Context context;
	
	private Spinner spinnerSelectCommand;
	private Spinner spinnerClientCertificate;
	
	private EditText etInstanceText;
	private EditText etPortText;
	private EditText etCertificatePasswordText;
	
	private Button btnRunCommand;
	
	
	private String command;
	private String certificateFileName;
	private String instanceString;
	private String passString;
	
	private Integer portNumber;
	private Boolean isClientCertificateUsed = false;
	private List<String> fileList = new ArrayList<String>();
	public RunCommandFragment(){
		
	}
	
	public RunCommandFragment(Context context){
		this.context = context;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_run_command, container, false);
        int i = getArguments().getInt(ACTIONBAR_TITLE);
        String actionbarTitle = getResources().getStringArray(R.array.menu_array)[i]; 
        FileManager fManager = new FileManager(context);
        File certificateDir = new File(fManager.getCertificateDirectory());
        
        File[] certificates = certificateDir.listFiles();
        for(File file : certificates){
        	fileList.add(file.getName());
        }
        
        
        
        spinnerClientCertificate = (Spinner) rootView.findViewById(R.id.spinner_certificate);
        ArrayAdapter<String> certAdapter = new ArrayAdapter<String>(getActivity(),
		        android.R.layout.simple_spinner_item, fileList);
        certAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClientCertificate.setAdapter(certAdapter);
        
        etCertificatePasswordText = (EditText) rootView.findViewById(R.id.et_certificate_pass);
        
        spinnerClientCertificate.setVisibility(View.INVISIBLE);
        etCertificatePasswordText.setVisibility(View.INVISIBLE);
        
        spinnerSelectCommand = (Spinner) rootView.findViewById(R.id.spinner_command);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.command_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinnerSelectCommand.setAdapter(adapter);
        
        getActivity().setTitle(actionbarTitle);
        return rootView;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		 
		CheckBox cbIncludeCertificate = (CheckBox) getActivity().findViewById(R.id.cb_include_certificate);
		if(fileList.isEmpty()){
			cbIncludeCertificate.setEnabled(false);
		}
		else{
			cbIncludeCertificate.setEnabled(true);
		}
		btnRunCommand = (Button) getActivity().findViewById(R.id.btn_run_command);
		btnRunCommand.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				etInstanceText = (EditText) getActivity().findViewById(R.id.et_instance_input);
				etPortText = (EditText) getActivity().findViewById(R.id.et_port_input);
				
				
				String portString;
				
				
				instanceString = etInstanceText.getText().toString();
				portString = etPortText.getText().toString();
				passString = etCertificatePasswordText.getText().toString();
				
				if(checkHostValidity(instanceString) && checkPortValidity(portString))
				{
					if(isClientCertificateUsed){
						if(checkPasswordValidity(passString)){
							try
							{
								portNumber = Integer.parseInt(portString);
								executeCommandTask();
								Log.i("Host Instance Details ","URL -  "+ instanceString +" Port - "+ portNumber + "command = "+command);
								
							}
							catch(NumberFormatException e)
							{
								Toast toast = Toast.makeText(getActivity().getApplicationContext(), context.getString(R.string.port_error), Toast.LENGTH_LONG);
								toast.show();	
							}
						}						
						else{
							Toast.makeText(getActivity().getApplicationContext(), context.getString(R.string.rc_certificatepass_err), Toast.LENGTH_LONG).show();
							try
							{
								portNumber = Integer.parseInt(portString);
								executeCommandTask();
								Log.i("Host Instance Details ","URL -  "+ instanceString +" Port - "+ portNumber + "command = "+command);
								
							}
							catch(NumberFormatException e)
							{
								Toast toast = Toast.makeText(getActivity().getApplicationContext(), context.getString(R.string.port_error), Toast.LENGTH_LONG);
								toast.show();	
							}
						}
						
					}
					else if(!isClientCertificateUsed){
						try
						{
							portNumber = Integer.parseInt(portString);
							executeCommandTask();
							Log.i("Host Instance Details ","URL -  "+ instanceString +" Port - "+ portNumber + "command = "+command);
							
						}
						catch(NumberFormatException e)
						{
							Toast toast = Toast.makeText(getActivity().getApplicationContext(), context.getString(R.string.port_error), Toast.LENGTH_LONG);
							toast.show();	
						}
					}
					
					
				}
				else
				{
					Toast toast = Toast.makeText(getActivity().getApplicationContext(), context.getString(R.string.host_port_error), Toast.LENGTH_LONG);
					toast.show();
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
		
		spinnerSelectCommand.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				
				command = parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}

	
	
	@Override
	public void commandExecutionStart() {
		
		//Remove in future if it is not absolutely necessary
		
	}

	@Override
	public void commandExecutionEnd(String result, Boolean isException) {
			showResult(result, isException);
	}
	
	//Method for execute the command
	
	public void executeCommandTask(){
		
		Syslogng syslogng = new Syslogng();
		syslogng.setHostName(instanceString);
		syslogng.setPortNumber(Integer.toString(portNumber));
		syslogng.setCertificateFileName(certificateFileName);
		syslogng.setCertificatePassword(passString);
		
		if(isClientCertificateUsed){
			
			CommandTask commandTask = new CommandTask(this, getActivity(), syslogng, command);
			commandTask.execute();
		}
		else{
			CommandTask commandTask = new CommandTask(this, getActivity(), syslogng, command);
			commandTask.execute();
		}
		
		
	}
	
	//Method for displaying Error / Exception
	
	public void showResult(String message, Boolean isException){
		
		if(isException){
			new AlertDialog.Builder(getActivity())
		    .setTitle("Error")
		    .setMessage(message)
		    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            
		        }
		     }).setIconAttribute(android.R.attr.alertDialogIcon)
		     .show();	
		}
		else{
			new AlertDialog.Builder(getActivity())
		    .setTitle("Result")
		    .setMessage(message)
		    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            
		        }
		     }).setIconAttribute(android.R.attr.icon)
		     .show();
		}
		
		
	}
	
	public Boolean checkHostValidity(String instanceString){
		
		return (instanceString != null && !instanceString.equals(""));
	}
	
public Boolean checkPasswordValidity(String passString){
		
		return (passString != null && !passString.equals(""));
	}
	
	public Boolean checkPortValidity(String portString){
		
		return (portString != null && !portString.equals(""));
	}

}
