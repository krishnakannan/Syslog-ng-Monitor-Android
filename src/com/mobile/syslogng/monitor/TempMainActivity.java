package com.mobile.syslogng.monitor;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TempMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temp_main);
		
		
		//Hide activity name on ActionBar
		getActionBar().setDisplayShowTitleEnabled(false);
		
		//Collect the Instance Details
		final Button button = (Button) findViewById(R.id.add_instance);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				String instanceDetails;
				Integer portNumber;
				Intent tempConnect;
				EditText instanceText;
				EditText portText;
				
				instanceText = (EditText) findViewById(R.id.instance_input);
				portText = (EditText) findViewById(R.id.port_input);
				tempConnect = new Intent(getApplicationContext(), TempConnector.class);
				
				instanceDetails = instanceText.getText().toString();
				
				if(instanceDetails == null || instanceDetails == "")
				{
					Toast toast = Toast.makeText(getApplicationContext(), "Enter valid host details", Toast.LENGTH_LONG);
					toast.show();
				}
				else
				{
					
					try
					{
						portNumber = Integer.parseInt(portText.getText().toString());
						tempConnect.putExtra("Instance", instanceDetails);
						tempConnect.putExtra("Port", portNumber);
						Log.i("Host Instance Details ","URL -  "+ instanceDetails +" Port - "+ portNumber);
						startActivity(tempConnect);
					}
					catch(Exception e)
					{
						Toast toast = Toast.makeText(getApplicationContext(), "Port entered is not a Number", Toast.LENGTH_LONG);
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
	}
}
