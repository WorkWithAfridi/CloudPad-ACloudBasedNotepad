package com.kyotobytes.cloudpad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class noteDetails extends AppCompatActivity {
    private TextView noteTileInTitle;
    private TextView noteContentInDetail;
    Intent dataFromIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);


        Toolbar customToolbar = findViewById(R.id.toolBarForNoteDetails);
        setSupportActionBar(customToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteContentInDetail=findViewById(R.id.SavedNotesContentInDetail);
        noteTileInTitle=findViewById(R.id.savedNotesTitleinDetail);
        dataFromIntent=getIntent();
        noteTileInTitle.setText(dataFromIntent.getStringExtra("title"));
        noteContentInDetail.setText(dataFromIntent.getStringExtra("content"));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            startActivity(new Intent(noteDetails.this, NotesMain.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void GoToEditNote(View view) {
        Intent intent= new Intent(view.getContext(), editNote.class);
        intent.putExtra("title", dataFromIntent.getStringExtra("title"));
        intent.putExtra("content", dataFromIntent.getStringExtra("content"));
        intent.putExtra("noteId", dataFromIntent.getStringExtra("noteId"));
        startActivity(intent);
    }
}