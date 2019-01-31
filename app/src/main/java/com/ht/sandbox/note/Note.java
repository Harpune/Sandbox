package com.ht.sandbox.note;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "note")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String text;
    private String checkable;
    @ColumnInfo(name = "image_paths")
    private String imagePaths;
    @ColumnInfo(name = "created_at")
    private long createdAt;
    @ColumnInfo(name = "updated_at")
    private long updatedAt;
    @ColumnInfo(name = "last_viewed_at")
    private long lastViewedAt;
    private boolean favourite;

    @Ignore
    public Note(String title, String text, boolean favourite) {
        this.title = title;
        this.text = text;
        this.favourite = favourite;

        long time = System.currentTimeMillis();

        this.createdAt = time;
        this.updatedAt = time;
    }

    public Note() { }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCheckable() {
        return checkable;
    }

    public void setCheckable(String checkable) {
        this.checkable = checkable;
    }

    public String getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(String imagePaths) {
        this.imagePaths = imagePaths;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getLastViewedAt() {
        return lastViewedAt;
    }

    public void setLastViewedAt(long lastViewedAt) {
        this.lastViewedAt = lastViewedAt;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", checkable='" + checkable + '\'' +
                ", imagePaths='" + imagePaths + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", lastViewedAt=" + lastViewedAt +
                ", favourite=" + favourite +
                '}';
    }
}
