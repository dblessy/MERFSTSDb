package com.sjsu.hackathon.merfstsdb;

import java.sql.Timestamp;

public class Annotation {
    private int id;
    private String title;
    private String body;
    private Timestamp create_time;
    public Annotation(int id, String title, String body, Timestamp create_time) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.create_time = create_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    @Override
    public String toString() {
        return this.title + " " + this.body;
    }
}
