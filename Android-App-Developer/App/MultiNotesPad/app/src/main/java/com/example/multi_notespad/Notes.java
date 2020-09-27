package com.example.multi_notespad;

import androidx.annotation.NonNull;

public class Notes{
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String title;
    private String description;

    @NonNull
    public String toString() {
        return title + ": " + description;
    }
}
