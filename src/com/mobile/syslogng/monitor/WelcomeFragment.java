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

import java.util.Locale;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class WelcomeFragment extends Fragment {
    public static final String ACTIONBAR_TITLE = "menu_title";

    private Context context;
    
    public WelcomeFragment(Context context) {
    	this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
        int i = getArguments().getInt(ACTIONBAR_TITLE);
        String actionbarTitle = getResources().getStringArray(R.array.menu_array)[i];
        getActivity().setTitle(actionbarTitle);
        
        Button btnAddInstances = (Button) rootView.findViewById(R.id.btn_welcome_fragment_add_instance);
        btnAddInstances.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Bundle args = new Bundle();
		    	Fragment fragment = new AddUpdateSyslogngFragment(context, null);
				args.putInt(AddUpdateSyslogngFragment.ACTIONBAR_TITLE, 2);
				fragment.setArguments(args);
				getActivity().getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
				MainActivity.updateDrawer(2);
				
			}
		});
        
        return rootView;
    }
}