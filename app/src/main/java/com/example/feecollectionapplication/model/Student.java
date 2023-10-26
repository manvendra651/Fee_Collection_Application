/*
 Created by Intellij IDEA
 Author Name: KULDEEP SINGH (kuldeep506)
 Date: 14-11-2021
*/

package com.example.feecollectionapplication.model;

public class Student {
    private String id;
    private String name;
    private String phone;
    private String stop;
    private String course;
    private String year;
    private int fees;
    private String date;
    private boolean submitted;
    private String imageUrl;
    private String collectedBy;

    public Student() {
    }

    public Student(String id, String name, String phone, String stop, String course,
                   String year, int fees, String date, boolean submitted,
                   String imageUrl, String collectedBy) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.stop = stop;
        this.course = course;
        this.year = year;
        this.fees = fees;
        this.date = date;
        this.submitted = submitted;
        this.imageUrl = imageUrl;
        this.collectedBy = collectedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCollectedBy() {
        return collectedBy;
    }

    public void setCollectedBy(String collectedBy) {
        this.collectedBy = collectedBy;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", stop='" + stop + '\'' +
                ", course='" + course + '\'' +
                ", year='" + year + '\'' +
                ", fees=" + fees +
                ", date='" + date + '\'' +
                ", submitted=" + submitted +
                ", imageUrl='" + imageUrl + '\'' +
                ", collectedBy='" + collectedBy + '\'' +
                '}';
    }
}
