package com.mobile.syslogng.monitor;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ImportCertificateFragment extends Fragment {

	public static final String ACTIONBAR_TITLE = "menu_title";
	private static final int PICKFILE_CODE = 1;
	
    public ImportCertificateFragment() {
        // Empty constructor required for fragment subclasses
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
     
    	TextView tvPath = (TextView) getActivity().findViewById(R.id.tv_import_certificate_selected_file);
    	switch(requestCode){
    		case PICKFILE_CODE:
    			if(resultCode == getActivity().RESULT_OK){
    				String FilePath = data.getData().getPath();
    				tvPath.setText(FilePath);
    			}
    				
      
      break;
      
     }
    }
}
