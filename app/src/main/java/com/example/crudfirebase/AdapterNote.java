package com.example.crudfirebase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdapterNote extends RecyclerView.Adapter<AdapterNote.MyViewHolder> {
    private List<Note> noteList;
    private Activity activity;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public AdapterNote(List<Note> noteList, Activity activity) {
        this.noteList = noteList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterNote.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewNote = inflater.inflate(R.layout.list_note, parent, false);
        return new MyViewHolder(viewNote);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNote.MyViewHolder holder, int position) {
        final Note note = noteList.get(position);
        holder.tv_title.setText("Title : " + noteList.get(position).getTitle());
        holder.tv_description.setText("Description : " + note.getDescription());

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference.child("notes").child(note.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(activity, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity, "Penghapusan gagal dilakukan", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setMessage("Apakah yakin ingin menghapus data ini ? " + note.getTitle());
                builder.show();
            }
        });

        holder.card_note.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(view.getContext(), InsertNoteActivity.class);
                intent.putExtra("Title", note.getTitle());
                intent.putExtra("Description", note.getDescription());
                intent.putExtra("Key", note.getKey());
                holder.itemView.getContext().startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_description;
        Button btn_delete;
        CardView card_note;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_description = itemView.findViewById(R.id.tv_description);
            card_note = itemView.findViewById(R.id.card_note);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
