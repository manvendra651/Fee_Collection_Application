package com.example.feecollectionapplication.Registration;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.feecollectionapplication.R;
import com.example.feecollectionapplication.model.Student;
import com.example.feecollectionapplication.util.Util;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class MainRegistration extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();

    //getting internet connection info
    ConnectivityManager connectivityManager;

    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference imageStorageReference;
    FirebaseAuth auth;

    private String currentPhotoPath;
    private EditText name, phone, date;
    private AutoCompleteTextView course, stop;
    private Spinner year;
    private ImageView fullImage;
    private Uri imageUri;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_registration);
        Objects.requireNonNull(getSupportActionBar()).hide();

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        db = FirebaseFirestore.getInstance(); // connection to Firestore

        storage = FirebaseStorage.getInstance();
        imageStorageReference = storage.getReference();
        auth = FirebaseAuth.getInstance();

        name = findViewById(R.id.regiName);
        course = findViewById(R.id.regiCourse);
        year = findViewById(R.id.regiYear);
        phone = findViewById(R.id.regiPhone);
        stop = findViewById(R.id.regiStop);

        date = findViewById(R.id.regiDate);
        //setting current date to date field
        String currentDate = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());
        date.setText(currentDate);

        fullImage = findViewById(R.id.regiImage);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        Button capture = findViewById(R.id.regiCaptureImage);
        Button save = findViewById(R.id.regiSave);
        save.setVisibility(View.INVISIBLE);

        /** code to capture the image*/
        capture.setOnClickListener(v -> {
            if (name.getText().toString().isEmpty() || course.getText().toString().isEmpty() ||
                    phone.getText().toString().isEmpty() || phone.getText().toString().isEmpty()) {
                String stName = name.getText().toString().trim();
                String phNO = phone.getText().toString().trim();
                String StStop = stop.getText().toString().trim();
                String stCourse = course.getText().toString().trim();
                String RegistrationDate = date.getText().toString().trim();
                Toast.makeText(getApplicationContext(), "Fill Above Details", Toast.LENGTH_LONG).show();
                if (TextUtils.isEmpty(stName)) {
                    name.setError("Name can't be Empty");
                } else if (TextUtils.isEmpty(phNO)) {
                    phone.setError("Phone can't be Empty");
                } else if (TextUtils.isEmpty(StStop)) {
                    stop.setError("Stop can't be Empty");
                } else if (TextUtils.isEmpty(stCourse)) {
                    course.setError("course can't be Empty");
                } else if (TextUtils.isEmpty(RegistrationDate)) {
                    date.setError("Date can't be Empty");
                }
            } else {
                save.setVisibility(View.VISIBLE);
                String fileName = "studentPhoto";
                File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    File imageFile = File.createTempFile(fileName, ".jpg", storageDirectory);

                    currentPhotoPath = imageFile.getAbsolutePath();

                    imageUri = FileProvider.getUriForFile(this, "com.example.bus.fileprovider", imageFile);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        /** Adapter to set courses to Year Spinner*/
        ArrayAdapter<String> courseArrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, Util.year);
        courseArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year.setAdapter(courseArrayAdapter);

        /** Adapter to set courses to Course AutoCompleteTextViw*/
        setAdapter(course, Util.course);

        /** Adapter to set stops to stops AutoCompleteTextViw*/
        setAdapter(stop, Util.stops);

        /** open date picker dialog with current date and set selected date to date editText*/
        DatePickerDialog.OnDateSetListener date1 = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        /** to select date */
        date.setOnClickListener(v -> {
            new DatePickerDialog(MainRegistration.this, date1, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        /** save all the details*/
        save.setOnClickListener(v -> {
            if (activeNetworkInfo == null) {
                Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.VISIBLE);
            save.setVisibility(View.INVISIBLE);
            capture.setVisibility(View.INVISIBLE);
            String stName = name.getText().toString().trim();
            String phNO = phone.getText().toString().trim();
            String StStop = stop.getText().toString().trim();
            String stCourse = course.getText().toString().trim();
            String RegistrationDate = date.getText().toString().trim();
            String yr = year.getSelectedItem().toString().trim();
            Student student = new Student();
            // after Successfully adding image now adding data in database storage
            auth.signInWithEmailAndPassword("hard31259@gmail.com", "Bus@1234");
//                FirebaseUser user = auth.getCurrentUser();
//            Log.d("user", "onCreate: " + user.getEmail());

            String filename = "image" + Timestamp.now().getSeconds();
            imageStorageReference.child("Students")
                    .child(filename)
                    .putFile(imageUri).addOnSuccessListener((task -> {
                imageStorageReference.child("Students").child(filename).getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            student.setName(stName);
                            student.setPhone(phNO);
                            student.setStop(StStop);
                            student.setCourse(stCourse);
                            student.setYear(yr);
                            student.setDate(RegistrationDate);
                            student.setImageUrl(imageUrl);
                            final int generatedID = (int) (Math.random() * 1000);
                            student.setId(String.valueOf(generatedID));

                            // adding student object to database
                            db.collection("Students").add(student).addOnSuccessListener(s -> {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(MainRegistration.this, "Registration Successful\nID=" + generatedID
                                        , Toast.LENGTH_LONG).show();
//                                Intent intent =new Intent(MainRegistration.this,RegistrationList.class);
//                                startActivity(intent);
                                finish();
                            }).addOnFailureListener(e -> {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(MainRegistration.this, "Failed", Toast.LENGTH_LONG).show();
                            });
                        }).addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainRegistration.this, "Unable to get Url", Toast.LENGTH_LONG).show();
                });
            }));
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            fullImage.setImageURI(imageUri);
            Bitmap bitmapImage = BitmapFactory.decodeFile(currentPhotoPath);
            imageUri = Util.compress(this, bitmapImage);// reducing the size of image

        }
    }

    /**
     * Adapter to set stops to stops AutoCompleteTextViw
     */
    private void setAdapter(AutoCompleteTextView view, String[] arr) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, arr);
        view.setThreshold(1);
        view.setAdapter(adapter);
    }

    /**
     * method to set date selected from date picker to date editText
     */
    private void updateLabel() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date.setText(sdf.format(myCalendar.getTime()));
    }
}