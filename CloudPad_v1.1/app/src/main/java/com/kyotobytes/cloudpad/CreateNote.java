package com.kyotobytes.cloudpad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNote extends AppCompatActivity {
    private EditText title;
    private EditText note;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        Toolbar customToolbar = findViewById(R.id.toolBarForCreateNote);
        setSupportActionBar(customToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title=findViewById(R.id.createTitleForNote);
        note=findViewById(R.id.createContentForNote);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveNote(View view) {
        String stitle = title.getText().toString();
        String snote = note.getText().toString();
        if(stitle.length()>80){
            Toast.makeText(this, "Title cannot be longer than 80 words", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(stitle) || TextUtils.isEmpty(snote)){
            Toast.makeText(this, "Both Title and Content are required.", Toast.LENGTH_SHORT).show();
        }else{
            DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
            Map<String, Object> note = new HashMap<>();
            note.put("title", stitle);
            note.put("content", snote);
            documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(CreateNote.this, "Note saved successfully.", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(CreateNote.this, NotesMain.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateNote.this, "Sorry an error occurred.\nFailed to create note. :( \nData not saved to cloud.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}