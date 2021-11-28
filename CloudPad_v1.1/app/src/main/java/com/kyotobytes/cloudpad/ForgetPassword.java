package com.kyotobytes.cloudpad;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    private EditText forgotpassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        forgotpassword=findViewById(R.id.ForgotPasswordOfEmail);
        forgotpassword.setBackground(null);
        firebaseAuth=FirebaseAuth.getInstance();
    }

    public void ResetPassword(View view) {
        String email=forgotpassword.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Email cannot be empty!", Toast.LENGTH_SHORT).show();
        }else if(!isValid(email)){
            Toast.makeText(this, "Please enter a valid email address. :)", Toast.LENGTH_SHORT).show();
        }else{
            //Send email
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ForgetPassword.this, "An mail has been sent to "+email+" with the reset instructions.", Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(ForgetPassword.this, "Error occurred. Please contact the system admin at khondakarafridi35@gmail.com.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void Back(View view) {
        finish();
    }

    private boolean isValid(String email){
        String regex = "^(.+)@(.+)$";
        return email.matches(regex);
    }
}