package com.yandex.sprint_4.service;

import com.yandex.sprint_4.model.Task;

import java.util.List;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();
}
