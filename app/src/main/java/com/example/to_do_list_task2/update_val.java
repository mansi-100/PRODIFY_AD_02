package com.example.to_do_list_task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class update_val extends AppCompatActivity {
    EditText et;
    Button button;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_val);
        et=findViewById(R.id.et);
        button=findViewById(R.id.button);
        String key = getIntent().getStringExtra("key");
        String title = getIntent().getStringExtra("title");
        DatabaseReference dref= FirebaseDatabase.getInstance().getReference("movies").child(key);

        et.setText(title);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e=et.getText().toString();
                tasks_add a=new tasks_add(e);
                Map<String, Object> updates = new HashMap<>();
                updates.put("title", a.getTitle()); // Assuming getTitle() returns the title string

                dref.updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(update_val.this, "updated done", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(update_val.this, "Failure sorry", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}