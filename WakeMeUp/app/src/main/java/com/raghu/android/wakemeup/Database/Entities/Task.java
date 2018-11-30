package com.raghu.android.wakemeup.Database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.raghu.android.wakemeup.Database.Entities.Category;

import java.io.Serializable;
import java.util.Date;
import java.sql.Time;

@Entity
public class Task implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int taskId;

    String title;
    String description;
    Date date;
    int priority;
    Category category;
    boolean completed;

    public Task(int taskId, String title, String description, Date date, int priority, Category category, boolean completed) {
        this.taskId = taskId;
        this.title = title;
        this.description = description;
        this.date = date;
        this.priority = priority;
        this.category = category;
        this.completed = completed;
    }

    @Ignore
    public Task(String title, String description, Date date, int priority, Category category, boolean completed) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.priority = priority;
        this.category = category;
        this.completed = completed;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return new Time(date.getTime());
    }

//    public void setTime(Time time) {
//        this.time = time;
//    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", priority=" + priority +
                ", category=" + category +
                ", completed=" + completed +
                '}';
    }
}
