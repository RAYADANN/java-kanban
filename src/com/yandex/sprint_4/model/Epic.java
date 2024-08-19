package com.yandex.sprint_4.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtasks = new ArrayList<>();

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
        this.setTaskType(TaskTypes.EPIC);
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public void removeAllSubtasks() {
        subtasks.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtasks=" + getSubtasks() +
                '}';
    }
}