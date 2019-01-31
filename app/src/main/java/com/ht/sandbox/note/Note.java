package com.ht.sandbox.note;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "note")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private String title;
    private String content;
    @ColumnInfo(name = "created_at")
    private long createdAt;
    @ColumnInfo(name = "updated_at")
    private long updatedAt;
    private boolean prioritized;

    @Ignore
    public Note(String title, String content) {
        this.title = title;
        this.content = content;
        this.prioritized = false;

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

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public boolean isPrioritized() {
        return prioritized;
    }

    public void setPrioritized(boolean prioritized) {
        this.prioritized = prioritized;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", prioritized=" + prioritized +
                '}';
    }
}
