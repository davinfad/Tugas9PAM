package com.example.crudfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class InsertNoteActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvEmail;
    private TextView tvUid;
    private Button btnKeluar;
    private FirebaseAuth mAuth;
    private EditText etTitle;
    private EditText etDesc;
    private Button btnSubmit;
    private Button btnView;
    private Button btnUpdate;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String title;
    private String description;
    private String key;
    Note note;
    Note selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_note);
        tvEmail = findViewById(R.id.tv_email);
        tvUid = findViewById(R.id.tv_uid);
        btnKeluar = findViewById(R.id.btn_keluar);

        mAuth = FirebaseAuth.getInstance();
        btnKeluar.setOnClickListener(this);

        etTitle = findViewById(R.id.et_title);
        etDesc = findViewById(R.id.et_description);
        btnSubmit = findViewById(R.id.btn_submit);

        btnView = findViewById(R.id.btn_view);
        btnView.setOnClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        note = new Note();
        selectedNote = new Note();
        btnSubmit.setOnClickListener(this);

        btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(this);

        Intent intent = getIntent();
        title = intent.getStringExtra("Title");
        description = intent.getStringExtra("Description");
        key = intent.getStringExtra("Key");

        etTitle.setText(title);
        etDesc.setText(description);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            tvEmail.setText(currentUser.getEmail());
            tvUid.setText(currentUser.getUid());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_keluar:
                logOut();
                break;
            case R.id.btn_submit:
                submitData();
                break;
            case R.id.btn_view:
                Intent intent = new Intent(InsertNoteActivity.this, ViewNoteActivity.class);
                startActivity(intent);
            case R.id.btn_update:
                update();
        }
    }

    public void logOut(){
        mAuth.signOut();
        Intent intent = new Intent(InsertNoteActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //makesure user cant go back
        startActivity(intent);
    }

    public void submitData(){
        if (!validateForm()){
            return;
        }

        String title = etTitle.getText().toString();
        String desc = etDesc.getText().toString();

        Note baru = new Note(title, desc);

        databaseReference.child("notes").push().setValue(new Note(desc, title)).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(InsertNoteActivity.this, "Add data", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InsertNoteActivity.this, "Failed to Add data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(etTitle.getText().toString())) {
            etTitle.setError("Required");
            result = false;
        } else {
            etTitle.setError(null);
        }

        if (TextUtils.isEmpty(etDesc.getText().toString())) {
            etDesc.setError("Required");
            result = false;
        } else {
            etDesc.setError(null);
        }

        return result;
    }
    public void update() {

        String newTitle = etTitle.getText().toString();
        String newDesc = etDesc.getText().toString();

        databaseReference.child("notes").child(key).setValue(new Note(newDesc, newTitle)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(InsertNoteActivity.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InsertNoteActivity.this, "Data gagal untuk diupdate", Toast.LENGTH_SHORT).show();
            }
        });
    }

}