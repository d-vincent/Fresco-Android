package biome.fresco.Objects;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.services.concurrency.Task;

/**
 * Created by Drew McDonald on 6/29/2017.
 */

public class MilestoneObject {


    public MilestoneObject(){
        this.tasks = new ArrayList<>();
    }

    public void removeTask(String id){
        for (TaskObject task: tasks){
            if (task.getId()==id){
                tasks.remove(task);
            }
        }
    }

    public TaskObject getTask(String id){
        for (TaskObject task: tasks){
            if (task.getId()==id){
                return task;
            }
        }
        return null;
    }


    public String getAuthor() {
        return author;
    }

    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public Long getLastEditedTimestamp() {
        return lastEditedTimestamp;
    }

    public String getName() {
        return name;
    }

    public List<TaskObject> getTasks() {
        return tasks;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCreatedTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setLastEditedTimestamp(Long lastEditedTimestamp) {
        this.lastEditedTimestamp = lastEditedTimestamp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTasks(List<TaskObject> tasks) {
        this.tasks = tasks;
    }

    public void addTask (TaskObject task){
        tasks.add(task);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
    String author;
    Long createdTimestamp;
    String description;
    String dueDate;
    Long lastEditedTimestamp;
    String name;
    List<TaskObject> tasks;


//    @Override
//    public int compareTo(@NonNull MilestoneObject o) {
//        return o.createdTimestamp.compareTo(this.createdTimestamp);
//    }
}
