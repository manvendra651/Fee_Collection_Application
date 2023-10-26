package com.example.feecollectionapplication.full_details;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.feecollectionapplication.R;
import com.example.feecollectionapplication.fee_details.FeeList;
import com.example.feecollectionapplication.model.Student;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class FullDetails extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();

    // getting fireStore firebase database instances
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Students");

    String updateId;
    Student students;
    String keyId;
    int updateFees;

    private TextView id;
    private TextView name;
    private TextView phone;
    private TextView stop;

    private TextView course;
    private TextView year;
    private TextView date;
    private TextView fees;

    private ImageView image;

    private EditText feeSubmitDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_details);
        Objects.requireNonNull(getSupportActionBar()).hide();

        id = findViewById(R.id.fulDetailsID);
        name = findViewById(R.id.fullDetailsName);
        phone = findViewById(R.id.fullDetailsPhone);
        stop = findViewById(R.id.fullDetailsStop);

        course = findViewById(R.id.fullDetailsCourse);
        year = findViewById(R.id.fullDetaillsYear);
        date = findViewById(R.id.fullDetailsDate);

        fees = findViewById(R.id.fullDetailsDues);

        image = findViewById(R.id.fullDetailsImage);
        Button clearDues = findViewById(R.id.fullDetailsClearDues);

        // retrieving ID from list cardView adapter and setting value to the full list page
        keyId = getIntent().getStringExtra("id");

        /** open date picker dialog with current date and set selected date to date editText*/
        DatePickerDialog.OnDateSetListener date1 = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        /** setting all fields of a students using his ID */
        collectionReference.whereEqualTo("id", keyId).get().addOnCompleteListener(task -> {
            if (task.getResult() != null) {
                for (QueryDocumentSnapshot student : task.getResult()) {
                    updateId = student.getId();
                    students = student.toObject(Student.class);
                }
                //setting values to full details editTexts
                id.setText(students.getId());
                name.setText(students.getName());
                phone.setText(students.getPhone());
                stop.setText(students.getStop());
                course.setText(students.getCourse());
                year.setText(students.getYear());
                fees.setText(String.valueOf(students.getFees()));
                if (fees.getText().toString().equals("1600")) {
                    clearDues.setVisibility(View.INVISIBLE);
                }
                updateFees = students.getFees();
                date.setText(students.getDate());
                Picasso.get().load(students.getImageUrl()).fit()
                        .placeholder(R.drawable.ic_baseline_account_circle_24).into(image);
            }
        }).addOnFailureListener(e ->
                Toast.makeText(getApplicationContext(), "Failed To Fetch value at Moment", Toast.LENGTH_LONG)
                        .show());

//        clearDues.setVisibility(View.GONE);
        /** method to update the fees of student*/
        clearDues.setOnClickListener(v -> {
            LayoutInflater li = LayoutInflater.from(FullDetails.this);
            View promptsView = li.inflate(R.layout.clear_dues_popup, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FullDetails.this);
            EditText feesBar = promptsView.findViewById(R.id.payFeesBar);
            feeSubmitDate = promptsView.findViewById(R.id.payFeesDate);
            //setting current date to date field
            String currentDate = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());
            feeSubmitDate.setText(currentDate);
            // setting on click to date EditText to choose a date from calendar
            feeSubmitDate.setOnClickListener(v1 -> new DatePickerDialog(FullDetails.this, date1, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show());
            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);
            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("PAY",
                            (dialog, id) -> {
                                // Fetching values from Popup dialogue's EditTexts for updating
                                String fee = feesBar.getText().toString().trim();
                                int f = Integer.parseInt(fee);
                                String updatedDate = feeSubmitDate.getText().toString().trim();

                                updateFees = updateFees + f;

                                collectionReference.document(updateId)
                                        .update("fees", updateFees);
                                
                                collectionReference.document(updateId)
                                        .update("date", updatedDate).addOnSuccessListener(unused -> {
                                    Toast.makeText(getApplicationContext(), "Dues Cleared", Toast.LENGTH_LONG)
                                            .show();
                                    Intent intent = new Intent(getApplicationContext(),FeeList.class);
                                    startActivity(intent);
                                })
                                        .addOnFailureListener(e ->
                                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG)
                                                        .show());
                            })
                    .setNegativeButton("Cancel",
                            (dialog, id) -> {
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

    }

    /*** method to set date selected from date picker to date editText */
    private void updateLabel() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        feeSubmitDate.setText(sdf.format(myCalendar.getTime()));
    }
}