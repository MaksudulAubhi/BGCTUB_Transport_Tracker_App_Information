package com.example.bgctub_transport_tracker_app_information.ui.student;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bgctub_transport_tracker_app_information.AuthorityReportInfoActivity;
import com.example.bgctub_transport_tracker_app_information.R;
import com.example.bgctub_transport_tracker_app_information.StudentReportInfoActivity;
import com.example.bgctub_transport_tracker_app_information.adapter.CustomList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentReportFragment extends Fragment implements AdapterView.OnItemClickListener{
    private ListView studentReportListView;
    private ArrayList<String> studentReportList;
    private ArrayList<String> idList;
    private CustomList arrayAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String reportId;
    private DatabaseReference studentReportListDatabaseRef;
    private long totalStudentReport = 0;
    private TextView studentReportCountTextView;

    private StudentReportViewModel mViewModel;

    public static StudentReportFragment newInstance() {
        return new StudentReportFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.student_report_fragment, container, false);

        studentReportCountTextView=root.findViewById(R.id.auth_report_count_textView);

        //database ref and others**
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        final String DATABASE_PATH = "student_app" + "/" + "report_feedback";
        studentReportListDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);


        //for listView setup**
        studentReportListView = root.findViewById(R.id.studentReportListView);
        studentReportList = new ArrayList<String>();
        arrayAdapter = new CustomList(studentReportList, getContext());
        studentReportListView.setAdapter(arrayAdapter);
        getReportInfoAndPass();

        //for userId list**
        idList = new ArrayList<String>();


        studentReportListView.setOnItemClickListener(this);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StudentReportViewModel.class);
        // TODO: Use the ViewModel
    }


    //get All report id and fetch info summary and pass id to details information view**
    public void getReportInfoAndPass() {
        studentReportListDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {

                    //count total reportID
                    totalStudentReport = snapshot.getChildrenCount();

                    //get all id if available**
                    for (DataSnapshot userIdSnapShot : snapshot.getChildren()) {


                        //get all data from id**
                        reportId = userIdSnapShot.getKey();


                        try {
                            String time = snapshot.child(reportId).child("time").getValue().toString();
                            String report_title = snapshot.child(reportId).child("report_feedback_title").getValue().toString();


                            //add data to list**
                            studentReportList.add("Time: \n" + time
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
                    studentReportList = refreshArrayList(studentReportList);

                } catch (Exception exception) {
                    //if no data available give message**
                    Toast.makeText(getActivity(), "Sorry information not available", Toast.LENGTH_LONG).show();
                }

                studentReportCountTextView.setText("Total Report: "+ String.valueOf(totalStudentReport));
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

        Intent intent = new Intent(getActivity(), StudentReportInfoActivity.class);
        intent.putExtra("reportId", idList.get(position));
        startActivity(intent);
    }

}