package com.mobile.syslogng.monitor;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AddInstanceFragment extends Fragment {
    public static final String ACTIONBAR_TITLE = "menu_title";

    public AddInstanceFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_instance, container, false);
        int i = getArguments().getInt(ACTIONBAR_TITLE);
        String actionbarTitle = getResources().getStringArray(R.array.menu_array)[i];
       
        getActivity().setTitle(actionbarTitle);
        return rootView;
    }
}
