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

public class RunCommandFragment extends Fragment implements IExecuteCommandCallBack{
	
	public static final String ACTIONBAR_TITLE = "menu_title";
	
	private Context context;
	
	private Spinner selectCommand;
	
	private EditText instanceText;
	private EditText portText;
	
	private TextView titleTV;
	private TextView selectCommandTV;
	
	private Button runCommandBtn;
	
	private ProgressBar progressBar;
	
	private String command = "EMPTY";
	private String instanceString;
	private String portString;
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
       
        titleTV = (TextView) rootView.findViewById(R.id.textview_main_title);
        selectCommandTV = (TextView) rootView.findViewById(R.id.textview_select_command);
        
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressbar_runcommand);
        
        progressBar.setVisibility(View.INVISIBLE);
        
        selectCommand = (Spinner) rootView.findViewById(R.id.spinner_command);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.command_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		selectCommand.setAdapter(adapter);
        
        getActivity().setTitle(actionbarTitle);
        return rootView;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		runCommandBtn = (Button) getActivity().findViewById(R.id.btn_run_command);
		runCommandBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				instanceText = (EditText) getActivity().findViewById(R.id.et_instance_input);
				portText = (EditText) getActivity().findViewById(R.id.et_port_input);
				
				
				
				
				instanceString = instanceText.getText().toString();
				portString = portText.getText().toString();
				
				if(instanceString == null || instanceString.equals("") || portString == null || portString.equals(""))
				{
					Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Enter valid host/port details", Toast.LENGTH_LONG);
					toast.show();
				}
				else if(command.equals("EMPTY")){
					Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Input the Command to be executed", Toast.LENGTH_LONG);
					toast.show();
				}
				else
				{
					
					try
					{
						portNumber = Integer.parseInt(portString);
						callExecuteCommandTask();
						Log.i("Host Instance Details ","URL -  "+ instanceString +" Port - "+ portNumber + "command = "+command);
						
					}
					catch(NumberFormatException e)
					{
						Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Port entered is not a Number", Toast.LENGTH_LONG);
						toast.show();	
					}
					finally
					{
						instanceText.setText("");
						portText.setText("");
					}
					
					
				}			
			}
		});
		
		selectCommand.setOnItemSelectedListener(new OnItemSelectedListener(){

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
		titleTV.setVisibility(View.INVISIBLE);
		selectCommand.setVisibility(View.INVISIBLE);
		selectCommandTV.setVisibility(View.INVISIBLE);
		instanceText.setVisibility(View.INVISIBLE);
		portText.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void commandExecutionEnd(String result, Boolean isException) {
		titleTV.setVisibility(View.VISIBLE);
		selectCommand.setVisibility(View.VISIBLE);
		selectCommandTV.setVisibility(View.VISIBLE);
		instanceText.setVisibility(View.VISIBLE);
		portText.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.INVISIBLE);
		
		if(isException){
			showError(result);
		}
		else{
			Intent resultIntent = new Intent(context, ResultActivity.class);
			resultIntent.putExtra("Result", result);
			startActivity(resultIntent);
		}
		
	}
	
	//Method for execute the command
	
	public void callExecuteCommandTask(){
		
		ExecuteCommandTask executeCommandTask = new ExecuteCommandTask(this, getActivity().getApplicationContext(), instanceString, portNumber, command);
		executeCommandTask.execute();
	}
	
	//Method for displaying Error / Exception
	
	public void showError(String message){
		new AlertDialog.Builder(getActivity())
	    .setTitle("Error")
	    .setMessage(message)
	    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        }
	     }).setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}

}
