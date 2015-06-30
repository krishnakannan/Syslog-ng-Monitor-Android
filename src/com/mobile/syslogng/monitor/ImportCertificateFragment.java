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

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
    	PackageManager packageManager = getActivity().getPackageManager();
    	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        if(list.size() > 0){
        	startActivityForResult(intent,PICKFILE_CODE);
        }
        else{
        	Toast.makeText(context, getActivity().getString(R.string.file_manager_err), Toast.LENGTH_LONG).show();
        }
        
    }
    
    @SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	final Button btnImportFile = (Button) getActivity().findViewById(R.id.btn_import);
    	final EditText etFileName = (EditText) getActivity().findViewById(R.id.et_import_certificate_filename);
    	final TextView tvPath = (TextView) getActivity().findViewById(R.id.tv_import_certificate_selected_file);
    	final String inputFilePath;
    	switch(requestCode){
    		case PICKFILE_CODE:
    			if(resultCode == getActivity().RESULT_OK){
    				if(data.getData().getPath() != null){
    					inputFilePath = data.getData().getPath();
    				}
    				else{
    					inputFilePath = "";
    				}

    				tvPath.setText(inputFilePath);
    				btnImportFile.setVisibility(View.VISIBLE);
    				btnImportFile.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Boolean status;
							if(etFileName.getText().toString().equals("") || inputFilePath.equals("")){
								Toast.makeText(context, getActivity().getString(R.string.ic_destination_file_err), Toast.LENGTH_LONG).show(); 
							}
							else{
								FileManager fManager = new FileManager(context);
								status = fManager.copyFile(inputFilePath, fManager.getCertificateFile(etFileName.getText().toString()));
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
