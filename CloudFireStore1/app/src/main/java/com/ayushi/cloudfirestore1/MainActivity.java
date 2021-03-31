package com.ayushi.cloudfirestore1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESRIPTION = "description";

    EditText etTitle, etDescription;
    Button btnSave;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etDescription = findViewById(R.id.etDescription);
        etTitle = findViewById(R.id.etTitle);
        btnSave = findViewById(R.id.btnSave);
    }
    public void saveNote(View v){
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();

        Map<String, Object> note = new HashMap<>();
        note.put(KEY_TITLE,title);
        note.put(KEY_DESRIPTION, description);

        firestore.collection("Notebook").document("My First FireStore Project").set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.e(TAG,e.toString());
                    }
                });
    }

}