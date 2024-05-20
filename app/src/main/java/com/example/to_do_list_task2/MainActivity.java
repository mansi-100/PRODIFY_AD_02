package com.example.to_do_list_task2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//    String[] list={"l1","l2","l3","l4","i5","i6","i7","i8","i9","i10"};
    ListView listView;
    FloatingActionButton floatingActionButton;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listview);

        
        floatingActionButton=findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,add_task.class);
                startActivity(intent);
            }
        });
        List<Object> items = new ArrayList<>();

//
//        ArrayList<String> arr=new ArrayList<>();

        ArrayAdapter arrayAdapter=new ArrayAdapter(this, R.layout.layout_list,R.id.textView4,items);
        listView.setAdapter(arrayAdapter);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference("movies");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    tasks_add movie = dataSnapshot.getValue(tasks_add.class);
                    if (movie != null) {
                        // Add the title to the list
                        items.add(movie.getTitle());
//
                    }

                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected= items.get(i).toString();
                Query query=databaseReference.orderByChild("title").equalTo(selected);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                            String key=dataSnapshot.getKey();
                            DatabaseReference dref=databaseReference.child(key);
                            Intent intent=new Intent(MainActivity.this,update_val.class);

                            intent.putExtra("key",key);
                            intent.putExtra("title",selected);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "Fill the new data", Toast.LENGTH_SHORT).show();
//                             items.set(i, updatedTaskTitle);
//                             arrayAdapter.notifyDataSetChanged();
//                            dref.child("title").setValue()

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                return  true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected= items.get(i).toString();
                Log.e("selected is ",selected);
                Query query = databaseReference.orderByChild("title").equalTo(selected);
               query.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                           String key=dataSnapshot.getKey(); //to retrive key
                           if(key!=null){
                               databaseReference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                        databaseReference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                items.remove(selected);
                                                arrayAdapter.notifyDataSetChanged();
                                                Toast.makeText(MainActivity.this, "Item removed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                   }
                               }) .addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(MainActivity.this, "Failed to remove item", Toast.LENGTH_SHORT).show();
                                   }
                               });
                           }
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });


            }
        });


    }
}