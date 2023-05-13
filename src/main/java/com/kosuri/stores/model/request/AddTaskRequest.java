package com.kosuri.stores.model.request;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;

public class AddTaskRequest extends RequestEntity<AddTaskRequest> {
    public AddTaskRequest(HttpMethod method, URI url) {
        super(method, url);
    }

    @NotNull
    private int taskId;
    @NotNull
    private String taskName;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
