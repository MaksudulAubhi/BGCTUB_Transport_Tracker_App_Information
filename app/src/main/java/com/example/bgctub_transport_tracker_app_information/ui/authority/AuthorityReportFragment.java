package com.example.bgctub_transport_tracker_app_information.ui.authority;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bgctub_transport_tracker_app_information.AuthorityInfoDetailsActivity;
import com.example.bgctub_transport_tracker_app_information.AuthorityReportInfoActivity;
import com.example.bgctub_transport_tracker_app_information.R;
import com.example.bgctub_transport_tracker_app_information.adapter.CustomList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AuthorityReportFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView authorityReportListView;
    private ArrayList<String> authorityReportList;
    private ArrayList<String> idList;
    private CustomList arrayAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String reportId;
    private DatabaseReference authorityReportListDatabaseRef;
    private long totalAuthorityReport = 0;
    private TextView authReportCountTextView;
    private AuthorityReportViewModel mViewModel;

    public static AuthorityReportFragment newInstance() {
        return new AuthorityReportFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.authority_report_fragment, container, false);

        authReportCountTextView=root.findViewById(R.id.auth_report_count_textView);

        //database ref and others**
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        final String DATABASE_PATH = "transport_authority_app" + "/" + "report_feedback";
        authorityReportListDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);


        //for listView setup**
        authorityReportListView = root.findViewById(R.id.authorityReportListView);
        authorityReportList = new ArrayList<String>();
        arrayAdapter = new CustomList(authorityReportList, getContext());
        authorityReportListView.setAdapter(arrayAdapter);
        getReportInfoAndPass();

        //for userId list**
        idList = new ArrayList<String>();


        authorityReportListView.setOnItemClickListener(this);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AuthorityReportViewModel.class);
        // TODO: Use the ViewModel
    }

    //get All report id and fetch info summary and pass id to details information view**
    public void getReportInfoAndPass() {
        authorityReportListDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {

                    //count total reportID
                    totalAuthorityReport = snapshot.getChildrenCount();

                    //get all id if available**
                    for (DataSnapshot userIdSnapShot : snapshot.getChildren()) {


                        //get all data from id**
                        reportId = userIdSnapShot.getKey();


                        try {
                            String time = snapshot.child(reportId).child("time").getValue().toString();
                            String report_title = snapshot.child(reportId).child("report_feedback_title").getValue().toString();


                            //add data to list**
                            authorityReportList.add("Time: \n" + time
                                    + "\n" + "Report Title: \n" + report_title
                            );
                            //add id to list**
                            idList.add(reportId);
                        } catch (Exception ex) {

                        }

                        //notify if data change**
                        arrayAdapter.notifyDataSetChanged();
                    }

                    //refresh the list to remove duplicate**
                    authorityReportList = refreshArrayList(authorityReportList);

                } catch (Exception exception) {
                    //if no data available give message**
                    Toast.makeText(getActivity(), "Sorry information not available", Toast.LENGTH_LONG).show();
                }

                authReportCountTextView.setText("Total Report: "+ String.valueOf(totalAuthorityReport));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    //refresh arrayList for remove duplicate data**
    public <String> ArrayList<String> refreshArrayList(ArrayList<String> list) {
        ArrayList<String> newList = new ArrayList<String>();
        for (String data : list) {
            if (!newList.contains(data)) {
                newList.add(data);
            }
        }

        return newList;

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //get report id with listView position because two concept in arrayList same position**
        //get report id and sent to details information activity**

        Intent intent = new Intent(getActivity(), AuthorityReportInfoActivity.class);
        intent.putExtra("reportId", idList.get(position));
        startActivity(intent);
    }

}