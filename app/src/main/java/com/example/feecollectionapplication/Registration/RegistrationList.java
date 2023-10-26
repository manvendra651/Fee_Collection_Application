/*
 Created by Intellij IDEA
 Author Name: KULDEEP SINGH (kuldeep506)
 Date: 15-09-2021
*/

package com.example.feecollectionapplication.Registration;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feecollectionapplication.R;
import com.example.feecollectionapplication.model.Student;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class RegistrationList extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Students");
    private TextView studentCount;
    private EditText searchBar;
    private ImageView searchBtn;
    private ImageView noRecordFound;
    private ArrayList<Student> list;
    private RecyclerView recyclerView;
    private RegistrationListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_student_list);
        Objects.requireNonNull(getSupportActionBar()).hide();

        searchBar = findViewById(R.id.regiListSearch);
        searchBtn = findViewById(R.id.searchImageBtn);
        noRecordFound=findViewById(R.id.Image_no_record_found);
        noRecordFound.setVisibility(View.GONE);

        studentCount = findViewById(R.id.listCount);
        list = new ArrayList<>();


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /** getting all the students from database and adding them to list*/
        collectionReference.get().addOnCompleteListener(task -> {
            noRecordFound.setVisibility(View.GONE);
            if (task.getResult() != null) {
                for (QueryDocumentSnapshot student : task.getResult()) {
                    Student students = student.toObject(Student.class);
                    list.add(students);
                }
                adapter = new RegistrationListAdapter(list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                studentCount.setText(String.valueOf(list.size()));
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Failed to get List", Toast.LENGTH_LONG).show();
        });

        /** method which search student by their name */
        searchBtn.setOnClickListener(v -> {
            noRecordFound.setVisibility(View.GONE);
            ArrayList<Student> filteredList = new ArrayList<>();
            String value = searchBar.getText().toString().trim();
            collectionReference.whereEqualTo("name", value).get().addOnCompleteListener(task -> {
                if (task.getResult() != null) {
                    for (QueryDocumentSnapshot student : task.getResult()) {
                        Student students = student.toObject(Student.class);
                        filteredList.add(students);
                    }
                    adapter = new RegistrationListAdapter(filteredList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    studentCount.setText(String.valueOf(filteredList.size()));
                }
                if(filteredList.size()==0){
                    Toast.makeText(getApplicationContext(), "No Student Found", Toast.LENGTH_LONG).show();
                    noRecordFound.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show());
        });
    }
}