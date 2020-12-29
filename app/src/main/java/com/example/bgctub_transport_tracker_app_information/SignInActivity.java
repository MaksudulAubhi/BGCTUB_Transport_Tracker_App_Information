package com.example.bgctub_transport_tracker_app_information;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bgctub_transport_tracker_app_information.services.BuildNotificationService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText signInEmailEditText,signInPasswordEditText;
    private Button signInButton;
    private TextView helpReqTextView;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInEmailEditText=findViewById(R.id.signin_email_editText);
        signInPasswordEditText=findViewById(R.id.signin_password_editText);
        signInButton=findViewById(R.id.signin_button);
        helpReqTextView=findViewById(R.id.signinhelpRequestTextView);

        signInButton.setOnClickListener(this);
        helpReqTextView.setOnClickListener(this);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();

        //check user logon or not
        if(mUser!=null && mUser.isEmailVerified()){
            startActivity(new Intent(this,AppMainActivity.class));
            finish();
        }

        progressDialog=new ProgressDialog(this);
        timer=new Timer();

        //Create internet checking notification
        checkInternet(SignInActivity.this);

        //build app notification
        buildAppNotification();

    }

    //user sign in method**
    public void userSignIn(){
        String email=signInEmailEditText.getText().toString().trim();
        String password=signInPasswordEditText.getText().toString().trim();

        //input validation**
        if(TextUtils.isEmpty(email)){
            signInEmailEditText.setError("Please enter your account email address.");
            return;
        }
        if(TextUtils.isEmpty(password)){
            signInPasswordEditText.setError("please enter your password.");
            return;
        }
        if(password.length()<6){
            signInPasswordEditText.setError("please enter correct password.");
            return;
        }


        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        //sign in with email and password
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //check verified email or not
                    if(mAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(SignInActivity.this,AppMainActivity.class));
                        finish();
                        Toast.makeText(SignInActivity.this,"Welcome "+mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                    }else{
                        alertDialogBuilder(SignInActivity.this);
                        Toast.makeText(SignInActivity.this,"Please verify your email address.",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(SignInActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    //send verification email if first time else resend verification email;
    //used send verification email method
    public void resendVerificationEmail(){
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    alertDialogBuilderSentSuccessMessage(SignInActivity.this);
                    Toast.makeText(SignInActivity.this,"Sent email successfully.",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(SignInActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }

                progressDialog.dismiss();
            }
        });
    }


    //alert dialog create request for to sent verification email**
    AlertDialog.Builder alertDialogBuilder(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Verification Account Message");
        builder.setMessage(R.string.resend_ver_email_message_req);
        builder.setIcon(R.drawable.ic_action_info);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resendVerificationEmail();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

        return builder;
    }

    //alert dialog for give success message for sent email verification

    AlertDialog.Builder alertDialogBuilderSentSuccessMessage(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Verification Account Message");
        builder.setMessage(R.string.resend_ver_email_message_success);
        builder.setIcon(R.drawable.ic_action_info);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

        return builder;
    }



    // Call Build app notification service
    private void buildAppNotification() {
        startService(new Intent(this, BuildNotificationService.class));
    }

    //*******************Check Internet and location********************
    //Check Internet Connection **
    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            android.net.NetworkInfo wifiNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mobileNetwork != null && mobileNetwork.isConnectedOrConnecting() || wifiNetwork != null && wifiNetwork.isConnectedOrConnecting()) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    // Internet connection check alert dialog message**

    AlertDialog.Builder alertDialogBuilderInternetConnection(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sorry, your phone not connected with internet");
        builder.setMessage(R.string.off_internet_message);
        builder.setIcon(R.drawable.ic_no_internet);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();

            }
        });
        builder.show();

        return builder;
    }

    //Build SnackBar notification for location check and internet connection check**
    void createSnackbar(View view, String message) {

        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }


    //create timer for checking connection a time period**
    private void checkInternet(final Context context) {
        if (!isConnected(context)) {
            alertDialogBuilderInternetConnection(context);
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isConnected(context)) {

                    createSnackbar(findViewById(R.id.signInLayout), getString(R.string.off_internet_message));

                }
            }
        }, 0, 10000);

    }


    @Override
    public void onClick(View v) {
        if(v==signInButton){
            //user sign in after click
            userSignIn();
        }
        if(v==helpReqTextView){
            //help activity open
            startActivity(new Intent(this,HelpActivity.class));
        }
    }
}