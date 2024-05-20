package com.example.to_do_list_task2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add_task extends AppCompatActivity {
EditText editText,editText2;
Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        editText=findViewById(R.id.editTextText);
//        editText2=findViewById(R.id.editTextText2);
        button=findViewById(R.id.button);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1,s2;
                s1=editText.getText().toString();
//                s2=editText2.getText().toString();
                tasks_add m=new tasks_add(s1);
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference databaseReference=firebaseDatabase.getReference("movies");
                databaseReference.push().setValue(m);
                Toast.makeText(add_task.this, "Data saved Thank you", Toast.LENGTH_SHORT).show();
            }
        });


        
    }
}