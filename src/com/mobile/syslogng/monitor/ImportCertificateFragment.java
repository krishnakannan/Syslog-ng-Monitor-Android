package com.mobile.syslogng.monitor;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ImportCertificateFragment extends Fragment {

	Context context;
	
	public static final String ACTIONBAR_TITLE = "menu_title";
	private static final int PICKFILE_CODE = 1;
	
    public ImportCertificateFragment() {
        // Empty constructor required for fragment subclasses
    }
    
    public ImportCertificateFragment(Context context) {
    	this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_import_certificate, container, false);
        int i = getArguments().getInt(ACTIONBAR_TITLE);
        String actionbarTitle = getResources().getStringArray(R.array.menu_array)[i];
       
        getActivity().setTitle(actionbarTitle);
        return rootView;
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        Button btnSelectFile = (Button) getActivity().findViewById(R.id.btn_select_file);
        btnSelectFile.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				selectFile();
			}
        	
        });
    }
	
    private void selectFile(){
    	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent,PICKFILE_CODE);
    }
    
    @SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	final Button btnImportFile = (Button) getActivity().findViewById(R.id.btn_import);
    	final EditText etFileName = (EditText) getActivity().findViewById(R.id.et_import_certificate_filename);
    	final TextView tvPath = (TextView) getActivity().findViewById(R.id.tv_import_certificate_selected_file);
    	switch(requestCode){
    		case PICKFILE_CODE:
    			if(resultCode == getActivity().RESULT_OK){
    				final String filePath = data.getData().getPath();
    				tvPath.setText(filePath);
    				btnImportFile.setVisibility(View.VISIBLE);
    				btnImportFile.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Boolean status;
							if(etFileName.getText().toString().equals("")){
								Toast.makeText(context, getActivity().getString(R.string.ic_destination_file_err), Toast.LENGTH_LONG).show(); 
							}
							else{
								FileManager fManager = new FileManager(context);
								status = fManager.copyFiles(filePath, etFileName.getText().toString());
								if(status){
									etFileName.setText("");
									tvPath.setText("");
									btnImportFile.setVisibility(View.INVISIBLE);
									Toast.makeText(context, getActivity().getString(R.string.ic_import_success), Toast.LENGTH_LONG).show();
								}
								else{
									Toast.makeText(context, getActivity().getString(R.string.ic_import_failure), Toast.LENGTH_LONG).show();
								}
							}
							
						}
					});
    			}
    				
      
      break;
      
     }
    }
}
