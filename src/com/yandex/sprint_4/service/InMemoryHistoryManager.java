package com.yandex.sprint_4.service;

import com.yandex.sprint_4.model.Task;

import java.util.ArrayList;
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
        Task t = new Task(task.getId(), task.getName(), task.getDescription(), task.getStatus());
        history.add(t);// Просто history.add(task) не подходит потому что он не сохраняет старые данные task;
    }

    @Override
    public List<Task> getHistory(){
        List<Task> historyCopy = history;
        return historyCopy;
    }


}
