/*
 Created by Intellij IDEA
 Author Name: KULDEEP SINGH (kuldeep506)
 Date: 13-09-2021
*/

package com.example.feecollectionapplication.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.feecollectionapplication.model.Student;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Util {
    public static String[] year = {"1", "2", "3", "4"};

    public static String[] course = {"B.Tech", "BCA", "MBA", "MCA", "Biotech", "BSc", "B.Pharm"
            , "BA", "B.Com", "BBA", "M.Pharm", "M.Tech", "MSc", "LLM","Diploma"};

    public static String[] stops = {"Krishnapuri", "Sadar", "Tank Chauraha", "RATC Flyover", "SBI Chauraha",
            "DD Plaza", "Bharatpur Gate", "Deeg Gate", "Roopam", "Masani","Agravatika","Radha Orchid", "Gokul Restaurant"};


    /**
     * method to reduce the size of image
     */
    public static Uri compress(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        int nh = (int) ( inImage.getHeight() * (512.0 / inImage .getWidth()) );
        Bitmap scaled = Bitmap.createScaledBitmap(inImage, 512, nh, true);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), scaled, "Student", null);
        return Uri.parse(path);
    }

    /**
     * method to reduce the calculate total fees
     */
    public static String calculateFees(ArrayList<Student> list) {
        int totalFee = 0;
        for (Student student : list) {
            totalFee = totalFee + student.getFees();
        }
        return String.valueOf(totalFee);
    }

}
