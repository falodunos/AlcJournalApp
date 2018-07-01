package com.example.syntax.alcjournalapp.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "notes")
public class NoteEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String content;
    private String description;
    private String tags;
    private Date createdAt;
    private Date updatedAt;
    private String author;

    @Ignore
    public NoteEntry(String title, String content, String description, String tags, String author, Date createdAt, Date updatedAt) {
        this.title = title;
        this.content = content;
        this.description = description;
        this.tags = tags;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.author = author;
    }

    public NoteEntry(int id, String title, String content, String description, String tags, String author, Date createdAt, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.description = description;
        this.tags = tags;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.author = author;
    }

    public int getId() { return  id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }

    public String getTags() { return tags; }

    public void setTags(String tags) { this.tags = tags; }

    public Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public String getAuthor() { return this.author; }
    public void setAuthor(String author) { this.author = author; }

}
