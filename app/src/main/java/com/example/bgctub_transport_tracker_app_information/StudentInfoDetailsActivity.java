package com.example.bgctub_transport_tracker_app_information;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentInfoDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private String userId;
    private TextView nameTextView, idTextView, genderTextView, departmentTextView, emailTextView, busStoppageTextView, programTextView, semesterTextView;
    private TextView copyEmailTextView,copyAllUsersInfoTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference studentInfoDatabaseRef;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info_details);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //get userId from transport information fragment
        userId = getIntent().getStringExtra("userId");

        nameTextView =findViewById(R.id.student_name_textview);
        idTextView = findViewById(R.id.student_id_textview);
        genderTextView = findViewById(R.id.student_gender_textview);
        departmentTextView = findViewById(R.id.student_department_textview);
        emailTextView = findViewById(R.id.student_email_textview);
        busStoppageTextView = findViewById(R.id.student_Bus_Stoppage_textview);
        programTextView = findViewById(R.id.student_program_textview);
        semesterTextView = findViewById(R.id.student_semester_textview);

        copyEmailTextView=findViewById(R.id.copy_student_email_textview);
        copyEmailTextView.setOnClickListener(this);

        copyAllUsersInfoTextView = findViewById(R.id.copy_student_users_info_textview);
        copyAllUsersInfoTextView.setOnClickListener(this);

        //firebase authentication and database reference**
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        final String DATABASE_PATH = "student_app" + "/" + "student_profile" + "/" + userId + "/" + "student_information";
        studentInfoDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);

        //add data to respected field
        addDataInField();
    }

    //add data to respective textView if data available**
    public void addDataInField() {
        studentInfoDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String name = snapshot.child("name").getValue().toString();
                    String id = snapshot.child("id").getValue().toString();
                    String gender = snapshot.child("gender").getValue().toString();
                    String department = snapshot.child("department").getValue().toString();
                    String bus_stoppage = snapshot.child("bus_stoppage").getValue().toString();
                    String program = snapshot.child("program").getValue().toString();
                    String semester = snapshot.child("semester").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();

                    nameTextView.setText(name);
                    idTextView.setText(id);
                    genderTextView.setText(gender);
                    departmentTextView.setText(department);
                    busStoppageTextView.setText(bus_stoppage);
                    programTextView.setText(program);
                    semesterTextView.setText(semester);
                    emailTextView.setText(email);

                } catch (Exception exception) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentInfoDetailsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v==copyEmailTextView){
            String contact= emailTextView.getText().toString().trim();
            if(contact!=null){
                //copy driver contact number to clipboard**
                ClipboardManager clipboardManager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Student Contact",contact);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(StudentInfoDetailsActivity.this,"contact email copied", Toast.LENGTH_LONG).show();
            }
        }
        if(v==copyAllUsersInfoTextView){
            String name = nameTextView.getText().toString().trim();
            String id = idTextView.getText().toString().trim();
            String gender = genderTextView.getText().toString().trim();
            String department = departmentTextView.getText().toString().trim();
            String bus_stoppage = busStoppageTextView.getText().toString().trim();
            String program = programTextView.getText().toString().trim();
            String semester = semesterTextView.getText().toString().trim();
            String email = emailTextView.getText().toString().trim();


            String allInformation = "Name:\n" + name + "\n\n"
                    + "Registration ID:\n" + id + "\n\n"
                    + "Faculty/ Department:\n" + department + "\n\n"
                    + "Program:\n" + program + "\n\n"
                    + "Semester:\n" + semester + "\n\n"
                    + "Gender:\n" + gender+ "\n\n"
                    + "Email:\n" + email;

            //copy driver contact number to clipboard**
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Student Information", allInformation);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(StudentInfoDetailsActivity.this, "User information copied", Toast.LENGTH_LONG).show();
        }
    }
}