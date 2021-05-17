package com.lakshmimanivannan.internship;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {

    Button sign_in,signup;
    TextInputLayout fname,pswd,email,uname, pno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        //hooks
        fname = (TextInputLayout) findViewById(R.id.f_name) ;
        uname = (TextInputLayout) findViewById(R.id.uname) ;
        email = (TextInputLayout) findViewById(R.id.email) ;
        pswd = (TextInputLayout) findViewById(R.id.pswd) ;
        pno = (TextInputLayout) findViewById(R.id.pno) ;


        sign_in = (Button) findViewById(R.id.sign_in1);
        signup = (Button) findViewById(R.id.signup1);


        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this,Login.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ValidateName()| !ValidateUname() | !ValidateEmail() | !ValidatePswd() | !ValidatePhoneNumber())
                {
                    return ;
                }

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("users");

                String First_name = fname.getEditText().getText().toString();
                String User_name = uname.getEditText().getText().toString();
                String Email = email.getEditText().getText().toString();
                String Password = pswd.getEditText().getText().toString();
                String Phone_Number = pno.getEditText().getText().toString();
                String Points = String.valueOf('0');
                String Level = String.valueOf('1');
                String Badge = "Bronze".toString() ;

                UserHelperClass helperClass = new UserHelperClass(First_name,User_name,Email,Password,Phone_Number,Points,Level,Badge);
                myRef.child(User_name).setValue(helperClass);


                String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

                UserHelperClass userHelperClass = new UserHelperClass(User_name,Points,Level,Badge,String.valueOf(0),timeStamp);
                DatabaseReference reference = database.getReference("rewards/"+User_name);
                reference.child(timeStamp).setValue(userHelperClass);

                Intent intent = new Intent(SignUp.this,DashBoard.class);
                intent.putExtra("fullname",First_name);
                intent.putExtra("username",User_name);
                intent.putExtra("email",Email);
                intent.putExtra("password",Password);
                intent.putExtra("number",Phone_Number);
                startActivity(intent);
            }

        });
    }

    private Boolean ValidateName() {
        String name1 = fname.getEditText().getText().toString();
        if(name1.isEmpty()){
            fname.setError("Field cannot be empty");
            return false;
        }
        else {
            fname.setError(null);
            fname.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean ValidateEmail() {
        String val = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()){
            email.setError("Field cannot be empty");
            return false;
        }
        else if(!val.matches(emailPattern)){
            email.setError("Invalid email address");
            return false;
        }
        else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean ValidatePswd() {
        String val = pswd.getEditText().getText().toString();
        String passwordVal = "^"+
                "(?=.*[a-zA-Z])"+
                "(?=.*[@#$%^&+=])"+
                "(?=\\S+$)"+
                ".{4,}"+
                "$";

        if(val.isEmpty()){
            pswd.setError("Field cannot be empty");
            return false;
        }
        else if (!val.matches(passwordVal)){
            pswd.setError("Password is too weak");
            return false;
        }
        else {
            pswd.setError(null);
            pswd.setErrorEnabled(false);
            return true;
        }
    }

    private boolean ValidatePhoneNumber() {
        String val = pno.getEditText().getText().toString().trim();
        String checkspaces = "\\A\\w{1,20}\\z";
        if (val.isEmpty()) {
            pno.setError("Enter valid phone number");
            return false;
        } else if (!val.matches(checkspaces)) {
            pno.setError("No White spaces are allowed!");
            return false;
        } else {
            pno.setError(null);
            pno.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean ValidateUname() {
        String val = uname.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";//"(?=\\s+$)";
        if(val.isEmpty()){
            uname.setError("Field cannot be empty");
            return false;
        }
        else if(val.length()>=15){
            uname.setError("Username is too long");
            return false;
        }
        else if (!val.matches(noWhiteSpace)){
            uname.setError("White spaces are not allowed");
            return false;
        }
        else{
            uname.setError(null);
            uname.setErrorEnabled(false);
            return true;
        }
    }
}