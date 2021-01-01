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

import com.example.bgctub_transport_tracker_app_information.DriverInfoDetailsActivity;
import com.example.bgctub_transport_tracker_app_information.R;
import com.example.bgctub_transport_tracker_app_information.StudentInfoDetailsActivity;
import com.example.bgctub_transport_tracker_app_information.adapter.CustomList;
import com.example.bgctub_transport_tracker_app_information.data_secure.DataSecure;
import com.example.bgctub_transport_tracker_app_information.ui.driver.DriverUsersViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentUsersFragment extends Fragment implements AdapterView.OnItemClickListener{

    private StudentUsersViewModel mViewModel;
    private ListView studentListView;
    private ArrayList<String> studentList;
    private ArrayList<String> idList;
    private CustomList arrayAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String userId;
    private DatabaseReference studentListDatabaseRef;
    private long totalStudentUsers = 0;
    private TextView studentUsersCountTextView;
    private DataSecure dataSecure;

    public static StudentUsersFragment newInstance() {
        return new StudentUsersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.student_users_fragment, container, false);

        studentUsersCountTextView=root.findViewById(R.id.student_users_count_textView);

        //for encoding and decoding
        dataSecure=new DataSecure();

        //database ref and others**
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        final String DATABASE_PATH = "student_app" + "/" + "student_profile";
        studentListDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);


        //for listView setup**
        studentListView = root.findViewById(R.id.studentListView);
        studentList = new ArrayList<String>();
        arrayAdapter = new CustomList(studentList, getContext());
        studentListView.setAdapter(arrayAdapter);
        getUserInfoAndPass();

        //for userId list**
        idList = new ArrayList<String>();


        studentListView.setOnItemClickListener(this);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(StudentUsersViewModel.class);
        // TODO: Use the ViewModel
    }

    //get All students id and fetch info summary and pass id to details information view**
    public void getUserInfoAndPass() {
        studentListDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    //count total userID
                    totalStudentUsers = snapshot.getChildrenCount();

                    //get all id if available**
                    for (DataSnapshot userIdSnapShot : snapshot.getChildren()) {


                        //get all data from id**
                        userId = userIdSnapShot.getKey();

                        try {
                            String student_id = dataSecure.dataDecode(snapshot.child(userId).child("student_information").child("id").getValue().toString());
                            String student_name = dataSecure.dataDecode(snapshot.child(userId).child("student_information").child("name").getValue().toString());
                            String student_department = dataSecure.dataDecode(snapshot.child(userId).child("student_information").child("department").getValue().toString());


                            //add data to list**
                            studentList.add("Student ID: " + student_id
                                    + "\n" + "Student Name: " + student_name
                                    + "\n" + "Student Department: " + student_department
                            );
                            //add id to list**
                            idList.add(userId);
                        } catch (Exception ex) {

                        }

                        //notify if data change**
                        arrayAdapter.notifyDataSetChanged();
                    }

                    //refresh the list to remove duplicate**
                    studentList = refreshArrayList(studentList);

                } catch (Exception exception) {
                    //if no data available give message**
                    Toast.makeText(getActivity(), "Sorry information not available", Toast.LENGTH_LONG).show();
                }
                studentUsersCountTextView.setText("Total Users: "+ String.valueOf(totalStudentUsers));
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
        //get user id with listView position because two concept in arrayList same position**
        //get user id and sent to details information activity**

        Intent intent = new Intent(getActivity(), StudentInfoDetailsActivity.class);
        intent.putExtra("userId", idList.get(position));
        startActivity(intent);
    }


}