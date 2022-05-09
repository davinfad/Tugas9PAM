package com.example.crudfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewNoteActivity extends AppCompatActivity {
    AdapterNote adapterNote;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    List<Note> noteArrayList;
    RecyclerView rv_view;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        rv_view = findViewById(R.id.rv_view);
        RecyclerView.LayoutManager managerLayout = new LinearLayoutManager(this);
        rv_view.setLayoutManager(managerLayout);
        rv_view.setItemAnimator(new DefaultItemAnimator());
        mAuth = FirebaseAuth.getInstance();
        
        viewNote();
    }

    private void viewNote() {
        databaseReference.child("notes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noteArrayList = new ArrayList<>();

                for(DataSnapshot item : dataSnapshot.getChildren()) {
                    Note note = item.getValue(Note.class);
                    note.setKey(item.getKey());
                    noteArrayList.add(note);
                }
                adapterNote = new AdapterNote(noteArrayList, ViewNoteActivity.this);
                rv_view.setAdapter(adapterNote);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}