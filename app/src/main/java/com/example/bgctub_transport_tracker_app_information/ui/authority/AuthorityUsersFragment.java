package com.example.bgctub_transport_tracker_app_information.ui.authority;

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

import com.example.bgctub_transport_tracker_app_information.AuthorityInfoDetailsActivity;
import com.example.bgctub_transport_tracker_app_information.R;
import com.example.bgctub_transport_tracker_app_information.StudentInfoDetailsActivity;
import com.example.bgctub_transport_tracker_app_information.adapter.CustomList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AuthorityUsersFragment extends Fragment implements AdapterView.OnItemClickListener {

    private AuthorityUsersViewModel mViewModel;
    private ListView authorityListView;
    private ArrayList<String> authorityList;
    private ArrayList<String> idList;
    private CustomList arrayAdapter;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String userId;
    private DatabaseReference authorityListDatabaseRef;
    private long totalAuthorityUsers = 0;
    private TextView authorityUsersCountTextView;

    public static AuthorityUsersFragment newInstance() {
        return new AuthorityUsersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.authority_users_fragment, container, false);
        authorityUsersCountTextView=root.findViewById(R.id.authority_users_count_textView);

        //database ref and others**
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        final String DATABASE_PATH = "transport_authority_app" + "/" + "authority_info";
        authorityListDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);


        //for listView setup**
        authorityListView = root.findViewById(R.id.authorityListView);
        authorityList = new ArrayList<String>();
        arrayAdapter = new CustomList(authorityList, getContext());
        authorityListView.setAdapter(arrayAdapter);
        getUserInfoAndPass();

        //for userId list**
        idList = new ArrayList<String>();


        authorityListView.setOnItemClickListener(this);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AuthorityUsersViewModel.class);
        // TODO: Use the ViewModel
    }

    //get All authority id and fetch info summary and pass id to details information view**
    public void getUserInfoAndPass() {
        authorityListDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    //count total reportID
                    totalAuthorityUsers = snapshot.getChildrenCount();

                    //get all id if available**
                    for (DataSnapshot userIdSnapShot : snapshot.getChildren()) {


                        //get all data from id**
                        userId = userIdSnapShot.getKey();

                        try {
                            String name = snapshot.child(userId).child("profile").child("name").getValue().toString();
                            String office_no = snapshot.child(userId).child("profile").child("office_no").getValue().toString();
                            String post = snapshot.child(userId).child("profile").child("post").getValue().toString();


                            //add data to list**
                            authorityList.add("Authority Name: " + name
                                    + "\n" + "Job Post: " + post
                                    + "\n" + "Office Number: " + office_no

                            );
                            //add id to list**
                            idList.add(userId);
                        } catch (Exception ex) {

                        }

                        //notify if data change**
                        arrayAdapter.notifyDataSetChanged();
                    }

                    //refresh the list to remove duplicate**
                    authorityList = refreshArrayList(authorityList);

                } catch (Exception exception) {
                    //if no data available give message**
                    Toast.makeText(getActivity(), "Sorry information not available", Toast.LENGTH_LONG).show();
                }

                authorityUsersCountTextView.setText("Total Users: "+ String.valueOf(totalAuthorityUsers));
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

        Intent intent = new Intent(getActivity(), AuthorityInfoDetailsActivity.class);
        intent.putExtra("userId", idList.get(position));
        startActivity(intent);
    }

}