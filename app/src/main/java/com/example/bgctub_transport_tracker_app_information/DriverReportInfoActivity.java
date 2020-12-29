package com.example.bgctub_transport_tracker_app_information;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverReportInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private String reportId;
    private TextView reportTimeTextView, reportTitleTextView, reportInfoTextView, reportAppNameTextView,
            reportConfTextView, reportUserContactTextView, copyAllReportInfoTextView;

    private FirebaseAuth mAuth;
    private DatabaseReference driverAppReportDatabaseRef;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_report_info);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //get report Id from authority report list fragment
        reportId = getIntent().getStringExtra("reportId");

        reportTimeTextView = findViewById(R.id.driver_report_time_textview);
        reportTitleTextView = findViewById(R.id.driver_report_title_textview);
        reportInfoTextView = findViewById(R.id.driver_report_information_textview);
        reportAppNameTextView = findViewById(R.id.driver_report_appName_textview);
        reportConfTextView = findViewById(R.id.driver_report_phoneConf_textview);
        reportUserContactTextView = findViewById(R.id.driver_report_contact_textView);
        copyAllReportInfoTextView = findViewById(R.id.copy_driver_report_info_textview);

        copyAllReportInfoTextView.setOnClickListener(this);

        //firebase authentication and database reference**
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        final String DATABASE_PATH = "driver_app" + "/" + "report_feedback" + "/" + reportId;
        driverAppReportDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);


        //add data to respective textView if data available**
        addDataInField();
    }

    //add data to respective textView if data available**
    public void addDataInField() {
        driverAppReportDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String time = snapshot.child("time").getValue().toString();
                    String title = snapshot.child("report_feedback_title").getValue().toString();
                    String information = snapshot.child("report_feedback_info").getValue().toString();
                    String appName = snapshot.child("appNameVersion").getValue().toString();
                    String phone_conf = snapshot.child("phone_configuration").getValue().toString();
                    String phone = snapshot.child("userPhone").getValue().toString();

                    reportTimeTextView.setText(time);
                    reportTitleTextView.setText(title);
                    reportInfoTextView.setText(information);
                    reportAppNameTextView.setText(appName);
                    reportConfTextView.setText(phone_conf);
                    reportUserContactTextView.setText(phone);


                } catch (Exception exception) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DriverReportInfoActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View v) {

        if (v == copyAllReportInfoTextView) {

            String time = reportTimeTextView.getText().toString().trim();
            String title = reportTitleTextView.getText().toString().trim();
            String information = reportInfoTextView.getText().toString().trim();
            String appName = reportAppNameTextView.getText().toString().trim();
            String phone_conf = reportConfTextView.getText().toString().trim();
            String user_contact = reportUserContactTextView.getText().toString().trim();

            String allInformation = "Time:\n" + time + "\n\n\n"
                    + "Title:\n" + title + "\n\n"
                    + "Information:\n" + information + "\n\n\n"
                    + "App:\n" + appName + "\n\n"
                    + "Phone Configuration:\n" + phone_conf + "\n\n"
                    + "User Contact:\n" + user_contact;

            //copy driver contact number to clipboard**
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Student Report Information", allInformation);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(DriverReportInfoActivity.this, "Report information copied", Toast.LENGTH_LONG).show();

        }
    }
}