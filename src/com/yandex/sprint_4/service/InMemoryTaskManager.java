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
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

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
        history.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public void createTask(Task task) {
        if (getTaskIntersection(task)) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Задача не подходит " + task);
        }

    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        history.remove(id);
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
        history.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void createEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);

    }

    @Override
    public void deleteEpicById(int id) {
        List<Subtask> subs = epics.get(id).getSubtasks();
        subs.forEach(subtask -> subtasks.remove(subtask.getId(), subtask));
        epics.remove(id);
        history.remove(id);
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        epics.values().forEach(epic -> {
            epic.removeAllSubtasks();
            updateEpicStatus(epic.getId());
        });
    }

    @Override
    public Subtask getSubtaskById(int id) {
        history.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (getTaskIntersection(subtask)) {
            subtasks.put(subtask.getId(), subtask);
            subtask.getEpic().addSubtask(subtask);
            updateEpicStatus(subtask.getEpic().getId());
        } else {
            System.out.println("Подзадача не подходит " + subtask);
        }
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
        history.remove(id);
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
        if (epic != null) {
            epic.setStatus(checkEpicStatus(epic));
            epic.getStartTime();
            epic.getEndTime();
        }
    }

    @Override
    public Status checkEpicStatus(Epic epic) {
        if (epic.getSubtasks().isEmpty()) {
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
    public List<Task> getHistory() {
        return new LinkedList<>(history.getHistory());
    }

    public TreeSet<Task> getPrioritizedTasks() {

        prioritizedTasks.addAll(getAllTasks());
        prioritizedTasks.addAll(getAllSubtasks());

        return prioritizedTasks;
    }

    public boolean getTaskIntersection(Task task) {
        return getPrioritizedTasks().stream().allMatch(task1 -> task.getStartTime().isAfter(task1.getEndTime())
                || task.getStartTime().isBefore(task1.getStartTime()));
    }
}
