package com.yandex.sprint_4.service;
import com.yandex.sprint_4.model.Epic;
import com.yandex.sprint_4.model.Status;
import com.yandex.sprint_4.model.Subtask;
import com.yandex.sprint_4.model.Task;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private int nextId = 1;

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Task createTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        return task;
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }


    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Epic createEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        return epic;
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);

    }

    public void deleteEpicById(int id) {
        List<Subtask> subs = epics.get(id).getSubtasks();
        for (Subtask sub : subs) {
            subtasks.remove(sub.getId(), sub);
        }
        epics.remove(id);
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic ep : epics.values()) {
            ep.removeAllSubtasks();
            updateEpicStatus(ep.getId());
        }
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        subtask.getEpic().addSubtask(subtask);
        updateEpicStatus(subtask.getEpic().getId());
        return subtask;
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpic().getId());
    }

    public void deleteSubtaskById(int id) {
        Epic ep = subtasks.get(id).getEpic();
        ep.removeSubtask(subtasks.get(id));
        updateEpicStatus(ep.getId());
        subtasks.remove(id);
    }

    public List<Subtask> getSubtasksByEpicId(int epicId) {
        return getAllSubtasks().stream()
                .filter(subtask -> subtask.getEpic().getId() == epicId)
                .collect(Collectors.toList());
    }

    public void updateEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if(epic != null){
            epic.setStatus(checkEpicStatus(epic));
        }
    }

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
}
