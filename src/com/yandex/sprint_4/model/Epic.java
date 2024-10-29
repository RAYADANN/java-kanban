package com.yandex.sprint_4.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtasks = new ArrayList<>();
    private LocalDateTime endTime = LocalDateTime.MIN;

    public Epic(int id, String name, String description, Status status) {
        super(id,
                name,
                description,
                status,
                Duration.ZERO,
                LocalDateTime.MAX);
        this.setTaskType(TaskTypes.EPIC);
        getStartTime();
        getEndTime();
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
    public LocalDateTime getEndTime() {
        for (Subtask sub : subtasks) {
            if (sub.getEndTime().isAfter(endTime)) endTime = sub.getEndTime();
        }
        return endTime;
    }

    @Override
    public LocalDateTime getStartTime() {
        for (Subtask sub : subtasks) {
            if (sub.getStartTime().isBefore(startTime)) startTime = sub.startTime;
        }
        return startTime;
    }

    @Override
    public Duration getDuration() {
        duration = Duration.between(this.getStartTime(), this.getEndTime());
        return duration;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                ", subtasks=" + getSubtasks() +
                ", endTime=" + getEndTime() +
                '}';
    }
}