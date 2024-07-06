package com.yandex.sprint_4.service;

import com.yandex.sprint_4.model.Task;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> history = new LinkedList<>();
    private static final int HISTORY_LIST_SIZE = 10;

    @Override
    public void add(Task task) {
        history.removeIf(Objects::isNull);
        if(history.size() >= HISTORY_LIST_SIZE){
            history.removeFirst();
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory(){
        return new LinkedList<>(history);
    }


}
