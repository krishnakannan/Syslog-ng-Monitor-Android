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

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RunCommandFragment extends Fragment implements ICommandCallBack{
	
	public static final String ACTIONBAR_TITLE = "menu_title";
	
	private Context context;
	
	private Spinner spinnerSelectCommand;
	
	private EditText etInstanceText;
	private EditText etPortText;
	
	private Button btnRunCommand;
	
	
	private String command;
	private String instanceString;
	
	private Integer portNumber;
	
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
		
		 
		
		btnRunCommand = (Button) getActivity().findViewById(R.id.btn_run_command);
		btnRunCommand.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				etInstanceText = (EditText) getActivity().findViewById(R.id.et_instance_input);
				etPortText = (EditText) getActivity().findViewById(R.id.et_port_input);
				
				String portString;
				
				
				instanceString = etInstanceText.getText().toString();
				portString = etPortText.getText().toString();
				
				
				if(checkHostValidity(instanceString) && checkPortValidity(portString))
				{
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
					finally
					{
						//Add Anything in future based on needs
					}
				}
				else
				{
					Toast toast = Toast.makeText(getActivity().getApplicationContext(), context.getString(R.string.host_port_error), Toast.LENGTH_LONG);
					toast.show();
				}			
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
		
		CommandTask commandTask = new CommandTask(this, getActivity(), instanceString, portNumber, command);
		commandTask.execute();
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
	
	public Boolean checkPortValidity(String portString){
		
		return (portString != null && !portString.equals(""));
	}

}
