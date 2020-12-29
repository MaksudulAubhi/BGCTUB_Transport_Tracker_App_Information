package com.example.bgctub_transport_tracker_app_information.ui.about;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bgctub_transport_tracker_app_information.BuildConfig;
import com.example.bgctub_transport_tracker_app_information.R;

public class AboutFragment extends Fragment {
    private TextView verTextView;
    private AboutViewModel mViewModel;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.about_fragment, container, false);
        verTextView=root.findViewById(R.id.versionTextView);
        String version_name= BuildConfig.VERSION_NAME;
        //add version name to version textView**
        verTextView.setText("Version: "+version_name);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AboutViewModel.class);
        // TODO: Use the ViewModel
    }

}