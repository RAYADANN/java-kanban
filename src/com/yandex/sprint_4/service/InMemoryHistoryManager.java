package com.yandex.sprint_4.service;

import com.yandex.sprint_4.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> history = new ArrayList<>();
    private static final int HISTORY_LIST_SIZE = 9;
    @Override
    public void add(Task task) {
        history.removeIf(Objects::isNull);
        if(history.size() > HISTORY_LIST_SIZE){
            history.removeFirst();
        }
        Task t = new Task(task.getId(), task.getName(), task.getDescription(), task.getStatus());
        history.add(t);
    }

    @Override
    public List<Task> getHistory(){

        return history;
    }


}
