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

import android.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AboutFragment extends Fragment{

	private IMainActivity mainActivityCallBack;
	private Context context;
	
	public AboutFragment(){
		
	}
	
	public AboutFragment(IMainActivity mainActivityCallBack, Context context){
		this.context = context;
		this.mainActivityCallBack = mainActivityCallBack;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
	        int i = getArguments().getInt(MainActivity.FRAGMENT_POS);
	        String actionbarTitle = getResources().getStringArray(R.array.menu_array)[i];
	        getActivity().setTitle(actionbarTitle);
	        
	        return rootView;
	    }
}
