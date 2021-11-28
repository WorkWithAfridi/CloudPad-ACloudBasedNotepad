package com.kyotobytes.cloudpad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity {

    private EditText logInEmail;
    private EditText logInPassword;
    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        logInEmail=findViewById(R.id.loginemail);
        logInPassword=findViewById(R.id.loginpassword);
        logInEmail.setBackground(null);
        logInPassword.setBackground(null);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            startActivity(new Intent(LogIn.this, NotesMain.class));
        }
    }

    public void SignUp(View view) {
        startActivity(new Intent(LogIn.this, SignUp.class));
    }

    public void ForgotPassword(View view) {
        startActivity(new Intent(LogIn.this, ForgetPassword.class));
    }

    public void LogIn(View view) {
        String email = logInEmail.getText().toString().trim();
        String pass = logInPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email cannot be empty!", Toast.LENGTH_SHORT).show();
        }else if(!isValid(email)){
            Toast.makeText(this, "Please enter a valid email address. :)", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Password cannot be empty!", Toast.LENGTH_SHORT).show();
        }else if(pass.length()<7){
            Toast.makeText(this, "Password should not be less then 7 words!", Toast.LENGTH_SHORT).show();
        }else{
            //login user
            firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        checkMailVerification();
                    }else{
                        Toast.makeText(LogIn.this, "Incorrect credentials.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private boolean isValid(String email){
        String regex = "^(.+)@(.+)$";
        return email.matches(regex);
    }
    private void checkMailVerification(){
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified()){
            finish();
            startActivity(new Intent(LogIn.this, NotesMain.class));
            //take to main menu/ notes page.
        }else{
            Toast.makeText(this, "User not verified.\nPlease verify the email and try again.", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}