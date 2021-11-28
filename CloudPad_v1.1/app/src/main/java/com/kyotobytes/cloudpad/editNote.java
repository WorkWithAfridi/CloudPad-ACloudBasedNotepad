package com.kyotobytes.cloudpad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ObjectStreamException;
import java.util.HashMap;
import java.util.Map;

public class editNote extends AppCompatActivity {
    private Intent dataFromIntent;
    EditText editTitle, editContent;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        editTitle=findViewById(R.id.EditTitleForEditNote);
        editContent=findViewById(R.id.EditContentForEditNote);


        Toolbar customToolbar = findViewById(R.id.toolBarForEditNote);
        setSupportActionBar(customToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataFromIntent=getIntent();
        editTitle.setText(dataFromIntent.getStringExtra("title"));
        editContent.setText(dataFromIntent.getStringExtra("content"));
    }

    public void saveEditedNote(View view) {
        String editedTitle=editTitle.getText().toString();
        String editedContent=editContent.getText().toString();
        if(editedTitle.length()>80){
            Toast.makeText(this, "Title cannot be longer than 80 words", Toast.LENGTH_SHORT).show();
        } else if(editedTitle.equals("") || editedContent.equals("")){
            Toast.makeText(this, "Title or Content cannot be empty.", Toast.LENGTH_SHORT).show();
        }else if(dataFromIntent.getStringExtra("title").equals(editedTitle) && dataFromIntent.getStringExtra("content").equals(editedContent)){
            onBackPressed();
        }else{
            DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(dataFromIntent.getStringExtra("noteId"));
            Map<String, Object> note = new HashMap<>();
            note.put("title", editedTitle);
            note.put("content", editedContent);
            documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Intent intent= new Intent(view.getContext(), noteDetails.class);
                    intent.putExtra("title", editedTitle);
                    intent.putExtra("content", editedContent);
                    intent.putExtra("noteId", dataFromIntent.getStringExtra("noteId"));
                    finish();
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "CloudPad updated.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed to update CloudPad. \nPlease try again later.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}