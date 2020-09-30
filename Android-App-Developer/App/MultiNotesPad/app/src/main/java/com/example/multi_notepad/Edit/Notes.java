package com.example.multi_notepad.Edit;

import java.io.Serializable;

public class Notes implements Serializable {
    private String name;
    private String body;
    private String time;
    //non-default constructor
    public Notes(String title, String description, String dateTime) {
        this.name = title;
        this.body = description;
        this.time = dateTime;
    }
    //accessor and mutator methods
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
