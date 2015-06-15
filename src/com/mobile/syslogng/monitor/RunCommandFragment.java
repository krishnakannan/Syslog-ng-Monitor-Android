package com.mobile.syslogng.monitor;

import android.app.Fragment;
import android.content.Context;
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
import android.widget.Spinner;
import android.widget.Toast;

public class RunCommandFragment extends Fragment{
	
	public static final String ACTIONBAR_TITLE = "menu_title";
	
	private Context context;
	
	private Spinner selectCommand;
	
	private String command = "EMPTY";
	
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
		
		final Button button = (Button) getActivity().findViewById(R.id.btn_run_command);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				String instanceString;
				String portString;
				Integer portNumber;
				Intent tempConnect;
				EditText instanceText;
				EditText portText;
				
				
				instanceText = (EditText) getActivity().findViewById(R.id.et_instance_input);
				portText = (EditText) getActivity().findViewById(R.id.et_port_input);
				
				
				tempConnect = new Intent(getActivity().getApplicationContext(), TempConnector.class);
				
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
						tempConnect.putExtra("Instance", instanceString);
						tempConnect.putExtra("Port", portNumber);
						Log.i("Host Instance Details ","URL -  "+ instanceString +" Port - "+ portNumber + "command = "+command);
						startActivity(tempConnect);
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

}
