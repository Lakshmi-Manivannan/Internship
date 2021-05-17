package com.lakshmimanivannan.internship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button signup,sign_in;
    TextInputLayout username,pswd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup = (Button) findViewById(R.id.signup);
        sign_in = (Button) findViewById(R.id.sign_in);

        username = (TextInputLayout) findViewById(R.id.username);
        pswd = (TextInputLayout) findViewById(R.id.password);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,SignUp.class));
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ValidateUname() | !ValidatePswd()){
                    return;
                }
                else{
                    isUser();
                }
            }
        });

    }

    private Boolean ValidateUname() {
        String val = username.getEditText().getText().toString();
        if(val.isEmpty()){
            username.setError("Field cannot be empty");
            return false;
        }
        else{
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean ValidatePswd() {
        String val = pswd.getEditText().getText().toString();
        if(val.isEmpty()){
            pswd.setError("Field cannot be empty");
            return false;
        }
        else {
            pswd.setError(null);
            pswd.setErrorEnabled(false);
            return true;
        }
    }
    private void isUser(){
        final String Entered_uname = username.getEditText().getText().toString().trim();
        final String Entered_pswd = pswd.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query check_user = reference.orderByChild("uname").equalTo(Entered_uname);

        check_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    username.setError(null);
                    username.setErrorEnabled(false);
                    String pswd_db = snapshot.child(Entered_uname).child("pswd").getValue(String.class);
                    if(pswd_db.equals(Entered_pswd)){
                        Intent intent = new Intent(Login.this,DashBoard.class);
                        intent.putExtra("fullname",snapshot.child(Entered_uname).child("fname").getValue(String.class));
                        intent.putExtra("username",snapshot.child(Entered_uname).child("uname").getValue(String.class));
                        intent.putExtra("email",snapshot.child(Entered_uname).child("email").getValue(String.class));
                        intent.putExtra("password",snapshot.child(Entered_uname).child("pswd").getValue(String.class));
                        intent.putExtra("number",snapshot.child(Entered_uname).child("pno").getValue(String.class));
                        startActivity(intent);
                    }
                    else{
                        pswd.setError("Wrong Password");
                        pswd.requestFocus();
                    }
                }
                else{
                    username.setError("User doesn't exist");
                    username.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}