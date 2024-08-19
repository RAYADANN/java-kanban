package com.yandex.sprint_4.model;

public class Subtask extends Task {


    public Subtask(int id, String name, String description, Status status, Epic epic) {
        super(id, name, description, status);
        setEpic(epic);
        this.setTaskType(TaskTypes.SUB);
    }


    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epic=" + epic.getName() +
                '}';
    }
}

