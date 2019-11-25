package com.example.uxm_login;

public class user {
    private String email;
    private String password;
    private String name;
    private String student_id;

    public user() {
    }

    public user(String email, String password, String name, String student_id) {
        this.email=email;
        this.password=password;
        this.name=name;
        this.student_id=student_id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getStudent_id() {
        return student_id;
    }
}