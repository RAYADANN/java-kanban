package com.yandex.sprint_4.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {


    public Subtask(int id,
                   String name,
                   String description,
                   Status status,
                   Epic epic,
                   Duration duration,
                   LocalDateTime startTime) {
        super(id, name, description, status, duration, startTime );
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
                ", epic=" + getEpic().getName() +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
                '}';
    }
}

