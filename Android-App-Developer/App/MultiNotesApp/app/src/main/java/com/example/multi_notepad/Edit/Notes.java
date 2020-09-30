package com.example.multi_notepad.Edit;

import java.io.Serializable;

public class Notes implements Serializable {
    private String name;
    private String body;
    private String time;
    private String mainBody;
    //default constructor
    public Notes(){
        name = "";
        body = "";
        time = "";
        if (body.length() > 80){
            mainBody = body.substring(0, 79) + "...";
        }
        else {
            mainBody = body;
        }
    }
    //non-default constructor
    public Notes(String title, String description, String dateTime) {
        this.name = title;
        this.body = description;
        this.time = dateTime;
        if (body.length() > 80){
            mainBody = body.substring(0, 79) + "...";
        }
        else {
            mainBody = body;
        }
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
        if(body.length() > 80){
            mainBody = body.substring(0, 79) + "...";
        }
        else{
            mainBody = body;
        }
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getMainBody() {
        return this.mainBody;
    }
    public void setMainBody(String mainBody) {
        this.mainBody = mainBody;
    }
}
