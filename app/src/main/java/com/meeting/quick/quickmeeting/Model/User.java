package com.meeting.quick.quickmeeting.Model;

import java.util.ArrayList;
import java.util.Random;

public class User {

    private String uid;
    private String firstName;
    private String lastName;
    private String unqieCode;
    private String avaliablity;
    private String role;
    private String location;
    private ArrayList<String> professorList;
    private int professorCounter;
    private int studentPosition;
    public User() {
    }

    public User(String uid, String firstName, String lastName, String unqieCode, String avaliablity, String role, ArrayList<String> professorList,int studentPosition ) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.unqieCode = unqieCode;
        this.avaliablity = avaliablity;
        this.professorList= professorList;
        this.studentPosition=studentPosition;
        this.role = role;
    }

    public User(String uid, String firstName, String lastName, String unqieCode, String avaliablity, String role, String location, int professorCounter) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.unqieCode = unqieCode;
        this.avaliablity = avaliablity;
        this.role = role;
        this.location = location;
        this.professorCounter = professorCounter;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUnqieCode() {
        return unqieCode;
    }

    public void setUnqieCode() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        this.unqieCode = salt.toString();
    }

    public String getAvaliablity() {
        return avaliablity;
    }

    public void setAvaliablity(String avaliablity) {
        this.avaliablity = avaliablity;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public ArrayList<String> getProfessorList() {
        return professorList;
    }

    public void setProfessorList(ArrayList<String> professorList) {
        this.professorList = professorList;
    }

    public int getProfessorCounter() {
        return professorCounter;
    }

    public void setProfessorCounter(int professorCounter) {
        this.professorCounter = professorCounter;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getStudentPosition() {
        return studentPosition;
    }

    public void setStudentPosition(int studentPosition) {
        this.studentPosition = studentPosition;
    }
}
