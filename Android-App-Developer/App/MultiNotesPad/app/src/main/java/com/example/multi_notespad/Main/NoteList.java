package com.example.multi_notespad.Main;

import java.util.Date;

import androidx.annotation.NonNull;

public class NoteList {
    private String name;
    private Date dateTime;
    private String body;

    private static int ctr = 1;

    NoteList() {
        this.name = ctr + name;
        this.body = body + ctr;
        ctr++;
    }

    public String getName() {
        return name;
    }
    public String getBody() {
        return body;
    }
    @NonNull
    @Override
    public String toString() {
        return "NoteList {" +
                "name='" + name + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
