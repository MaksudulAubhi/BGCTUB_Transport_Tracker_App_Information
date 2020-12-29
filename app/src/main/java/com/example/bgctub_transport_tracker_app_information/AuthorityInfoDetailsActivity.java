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

public class AuthorityInfoDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private String userId;
    private TextView nameTextView,genderTextView,emailTextView,contactTextView,office_no_TextView,postTextView;
    private TextView copyEmailTextView,copyPhoneTextView,copyAllUsersInfoTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference authorityInfoDatabaseRef;
    private FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_info_details);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //get userId from authority users fragment
        userId=getIntent().getStringExtra("userId");

        nameTextView=findViewById(R.id.authority_name_textview);
        genderTextView=findViewById(R.id.authority_gender_textview);
        emailTextView=findViewById(R.id.authority_email_textview);
        contactTextView= findViewById(R.id.authority_contact_textview);
        office_no_TextView= findViewById(R.id.auth_office_num_textview);
        postTextView= findViewById(R.id.auth_job_post);

        copyEmailTextView=findViewById(R.id.copy_auth_email_textview);
        copyPhoneTextView=findViewById(R.id.copy_auth_contact_textview);
        copyAllUsersInfoTextView = findViewById(R.id.copy_auth_users_info_textview);

        copyAllUsersInfoTextView.setOnClickListener(this);
        copyPhoneTextView.setOnClickListener(this);
        copyEmailTextView.setOnClickListener(this);

        //firebase authentication and database reference**
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        final String DATABASE_PATH = "transport_authority_app" + "/"+"authority_info"+"/"+ userId +"/"+ "profile";
        authorityInfoDatabaseRef = FirebaseDatabase.getInstance().getReference(DATABASE_PATH);

        //add data to respected field
        addDataInField();
    }


    //add data to respective textView if data available**
    public void addDataInField() {
        authorityInfoDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String name = snapshot.child("name").getValue().toString();
                    String gender = snapshot.child("gender").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();
                    String contact=snapshot.child("contact").getValue().toString();
                    String office_no=snapshot.child("office_no").getValue().toString();
                    String post=snapshot.child("post").getValue().toString();

                    nameTextView.setText(name);
                    genderTextView.setText(gender);
                    emailTextView.setText(email);
                    contactTextView.setText(contact);
                    office_no_TextView.setText(office_no);
                    postTextView.setText(post);

                } catch (Exception exception) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AuthorityInfoDetailsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }



    @Override
    public void onClick(View v) {
        if(v==copyEmailTextView){
            String email= emailTextView.getText().toString().trim();
            if(email!=null){
                //copy driver contact number to clipboard**
                ClipboardManager clipboardManager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Authority Contact",email);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(AuthorityInfoDetailsActivity.this,"Contact email copied", Toast.LENGTH_LONG).show();
            }

        }
        if(v==copyPhoneTextView){
            String contact= contactTextView.getText().toString().trim();
            if(contact!=null){
                //copy driver contact number to clipboard**
                ClipboardManager clipboardManager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Authority Contact",contact);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(AuthorityInfoDetailsActivity.this,"Contact number copied", Toast.LENGTH_LONG).show();
            }
        }

        if(v==copyAllUsersInfoTextView){
            String name = nameTextView.getText().toString().trim() ;
            String gender = genderTextView.getText().toString().trim();
            String email = emailTextView.getText().toString().trim();
            String contact= contactTextView.getText().toString().trim();
            String office_no= office_no_TextView.getText().toString().trim();
            String post= postTextView.getText().toString().trim();


            String allInformation = "Name:\n" + name + "\n\n"
                    + "Gender:\n" + gender + "\n\n"
                    + "Email:\n" + email + "\n\n"
                    + "Contact Number:\n" + contact + "\n\n"
                    + "Office Number:\n" + office_no + "\n\n"
                    + "Job Post:\n" + post;

            //copy driver contact number to clipboard**
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Authority Information", allInformation);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(AuthorityInfoDetailsActivity.this, "User information copied", Toast.LENGTH_LONG).show();
        }
    }
}