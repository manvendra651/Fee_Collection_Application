package com.example.feecollectionapplication.fee_details;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feecollectionapplication.R;
import com.example.feecollectionapplication.model.Student;
import com.example.feecollectionapplication.util.Util;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class FeeList extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Students");

    String[] search = {"id","name", "stop","Dues","All"};
    FeeListAdapter adapter;
    private ArrayList<Student> filteredList;
    private ArrayList<Student> list;

    private TextView studentCount;
    private TextView totalFees;
    private Spinner selectSearchSpinner;
    private EditText searchBar;
    private ImageView searchBtn;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_list);
        Objects.requireNonNull(getSupportActionBar()).hide();

        filteredList = new ArrayList<>();
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.feeRecyclerView);

        studentCount = findViewById(R.id.feeListTotalStudent);
        totalFees = findViewById(R.id.feeTotalAmount);
        selectSearchSpinner = findViewById(R.id.feeSearchSelectorSpinner);
        searchBar = findViewById(R.id.feeListSearchBar);
        searchBtn = findViewById(R.id.FeeListSearchImageBtn);
        totalFees = findViewById(R.id.feeTotalAmount);


        /** setting the adapter to search spinner */
        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, search);
        searchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSearchSpinner.setAdapter(searchAdapter);

        /** setting the data of all the students from server*/
        recyclerView = findViewById(R.id.feeRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        collectionReference.get().addOnCompleteListener(task -> {
            if (task.getResult() != null) {
                for (QueryDocumentSnapshot student : task.getResult()) {
                    Student students = student.toObject(Student.class);
                    list.add(students);
                }
                adapter = new FeeListAdapter(list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                studentCount.setText(String.valueOf(list.size()));
                totalFees.setText(Util.calculateFees(list));
            }
        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed To Fetch value at Moment", Toast.LENGTH_LONG)
                .show());

        /** method to get filter search of students */
        searchBtn.setOnClickListener(v -> {
            String item = selectSearchSpinner.getSelectedItem().toString().trim();
            String value = searchBar.getText().toString().trim();
            if (item.equals("id")) {
                query(item, value);
            }
            if (item.equals("name")) {
                query(item, value);
            }
            if (item.equals("stop")) {
                query(item, value);
            }
            if (item.equals("Dues")) {
                dues();
            }
            if(item.equals("All")) {
                adapter = new FeeListAdapter(list);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                studentCount.setText(String.valueOf(list.size()));
            }
        });
    }

    /**
     * method which fetch filtered data from database and set it to the list
     */
    private void query(String field, String value) {
        filteredList=new ArrayList<>();
        collectionReference.whereEqualTo(field, value).get().addOnCompleteListener(task -> {
            if (task.getResult() != null) {
                for (QueryDocumentSnapshot student : task.getResult()) {
                    Student students = student.toObject(Student.class);
                    filteredList.add(students);
                }
                adapter = new FeeListAdapter(filteredList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                studentCount.setText(String.valueOf(filteredList.size()));
            }
            if (filteredList.size() == 0) {
                Toast.makeText(getApplicationContext(), "No Student Found", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**method to fetch list of students who have not submitted their fees */
    private void dues() {
        filteredList=new ArrayList<>();
        collectionReference.whereLessThan("fees",1600).get().addOnCompleteListener(task -> {
            if (task.getResult() != null) {
                for (QueryDocumentSnapshot student : task.getResult()) {
                    Student students = student.toObject(Student.class);
                    filteredList.add(students);
                }
                adapter = new FeeListAdapter(filteredList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                studentCount.setText(String.valueOf(filteredList.size()));
            }
            if (filteredList.size() == 0) {
                Toast.makeText(getApplicationContext(), "No Student Found", Toast.LENGTH_LONG).show();
            }
        });
    }
}