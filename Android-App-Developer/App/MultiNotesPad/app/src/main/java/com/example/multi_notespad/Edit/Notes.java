package com.example.multi_notespad.Edit;

import androidx.annotation.NonNull;

public class Notes{
    private String title;
    private String description;

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

    @NonNull
    public String toString() {
        return title + ": " + description;
    }
}
