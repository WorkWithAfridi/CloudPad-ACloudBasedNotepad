package com.kyotobytes.cloudpad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private EditText signUpEmail;
    private EditText signUpPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signUpEmail=findViewById(R.id.signupemail);
        signUpPassword=findViewById(R.id.signuppassword);
        signUpPassword.setBackground(null);
        signUpEmail.setBackground(null);
        firebaseAuth=FirebaseAuth.getInstance();
    }

    public void BackToLogin(View view) {
        finish();
    }

    public void SignUp(View view) {
        String email = signUpEmail.getText().toString().trim();
        String pass = signUpPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email cannot be empty!", Toast.LENGTH_SHORT).show();
        }else if(!isValid(email)){
            Toast.makeText(this, "Please enter a valid email address. :)", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
        }else if(pass.length()<7){
            Toast.makeText(this, "Password should not be less then 7 words!", Toast.LENGTH_SHORT).show();
        }else{
            //Sign up activity,  register the user in firebase
            firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SignUp.this, "Registration Successful.\nThank you for signing up with CloudPad.", Toast.LENGTH_SHORT).show();
                        sendEmailVerification();
                    }else{
                        Toast.makeText(SignUp.this, "Failed to register user.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    //email verification
    private boolean isValid(String email){
        String regex = "^(.+)@(.+)$";
        return email.matches(regex);
    }
    //send verification email
    private void sendEmailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(SignUp.this, "Sending verification email.\nPlease verify your email.", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                }
            });
        }else{
            Toast.makeText(this, "Error occurred during email verification process!", Toast.LENGTH_SHORT).show();
        }
    }
}