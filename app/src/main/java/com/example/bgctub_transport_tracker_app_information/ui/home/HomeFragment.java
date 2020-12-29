package com.example.bgctub_transport_tracker_app_information.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bgctub_transport_tracker_app_information.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView totalDriverUserCountTextView, totalStudentUserCountTextView, totalAuthorityUserCountTextView;
    private long totalDriverUser = 0, totalStudentUser = 0, totalAuthorityUser = 0;
    private FirebaseAuth mAuth;
    private DatabaseReference driverListDatabaseRef, studentListDatabaseRef, authorityListDatabaseRef ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        totalDriverUserCountTextView = root.findViewById(R.id.totalBusCount);
        totalStudentUserCountTextView= root.findViewById(R.id.studentUsersCount);
        totalAuthorityUserCountTextView= root.findViewById(R.id.authorityUsersCount);


        //check user and databaseReferences
        mAuth = FirebaseAuth.getInstance();

        final String DATABASE_PATH_DRIVER_APP = "driver_app" + "/" + "transport_info_location";
        driverListDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_DRIVER_APP);

        final String DATABASE_PATH_STUDENT_APP = "student_app" + "/" + "student_profile";
        studentListDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_STUDENT_APP);

        final String DATABASE_PATH_AUTHORITY_APP = "transport_authority_app" + "/" + "authority_info";
        authorityListDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_AUTHORITY_APP);

        //get total driver users**
        getDriverUserInfo();

        //count all student users**
        getStudentUserInfo();

        //count all authority users**
        getAuthorityUserInfo();

        return root;
    }

    //count all driver users users**
    public void getDriverUserInfo() {
        driverListDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {
                    //count total userID
                    totalDriverUser = snapshot.getChildrenCount();

                } catch (Exception exception) {
                    Toast.makeText(getActivity(), "Sorry no information available.", Toast.LENGTH_LONG).show();
                }

                totalDriverUserCountTextView.setText(String.valueOf(totalDriverUser));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    //count all student users**
    public void getStudentUserInfo() {
        studentListDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {
                    //count total userID
                    totalStudentUser = snapshot.getChildrenCount();

                } catch (Exception exception) {
                    Toast.makeText(getActivity(), "Sorry no information available.", Toast.LENGTH_LONG).show();
                }

                totalStudentUserCountTextView.setText(String.valueOf(totalStudentUser));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    //count all student users**
    public void getAuthorityUserInfo() {
        authorityListDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {
                    //count total userID
                    totalAuthorityUser = snapshot.getChildrenCount();

                } catch (Exception exception) {
                    Toast.makeText(getActivity(), "Sorry no information available.", Toast.LENGTH_LONG).show();
                }

                totalAuthorityUserCountTextView.setText(String.valueOf(totalAuthorityUser));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}