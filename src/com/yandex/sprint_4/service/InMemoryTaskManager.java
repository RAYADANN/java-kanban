package com.yandex.sprint_4.service;
import com.yandex.sprint_4.model.Epic;
import com.yandex.sprint_4.model.Status;
import com.yandex.sprint_4.model.Subtask;
import com.yandex.sprint_4.model.Task;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager history = Managers.getDefaultHistory();
    private int nextId = 1;

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public Task getTaskById(int id) {

        history.getHistory().add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Task createTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }


    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        history.getHistory().add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);

    }

    @Override
    public void deleteEpicById(int id) {
        List<Subtask> subs = epics.get(id).getSubtasks();
        for (Subtask sub : subs) {
            subtasks.remove(sub.getId(), sub);
        }
        epics.remove(id);
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic ep : epics.values()) {
            ep.removeAllSubtasks();
            updateEpicStatus(ep.getId());
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        history.getHistory().add(subtasks.get(id));
        return  subtasks.get(id);
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        subtask.getEpic().addSubtask(subtask);
        updateEpicStatus(subtask.getEpic().getId());
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpic().getId());
    }

    @Override
    public void deleteSubtaskById(int id) {
        Epic ep = subtasks.get(id).getEpic();
        ep.removeSubtask(subtasks.get(id));
        updateEpicStatus(ep.getId());
        subtasks.remove(id);
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        return getAllSubtasks().stream()
                .filter(subtask -> subtask.getEpic().getId() == epicId)
                .collect(Collectors.toList());
    }

    @Override
    public void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if(epic != null){
            epic.setStatus(checkEpicStatus(epic));
        }
    }

    @Override
    public Status checkEpicStatus(Epic epic){
        if(epic.getSubtasks().isEmpty()){
            return Status.NEW;
        }

        if (epic.getSubtasks().stream().allMatch(task -> task.getStatus().equals(Status.NEW))) {
            return Status.NEW;
        } else if (epic.getSubtasks().stream().allMatch(task -> task.getStatus().equals(Status.DONE))) {
            return Status.DONE;
        } else {
            return Status.IN_PROGRESS;
        }
    }

    @Override
    public List<Task> getHistory(){
        return history.getHistory();
    }
}
