import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
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
        task.id = nextId++;
        tasks.put(task.id, task);
        return task;
    }

    public void updateTask(Task task) {
        tasks.put(task.id, task);

    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }


    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    public Epic getEpicById(int id) {
        return (Epic) epics.get(id);
    }

    public Epic createEpic(Epic epic) {
        epic.id = nextId++;
        epics.put(epic.id, epic);
        return epic;
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.id, epic);
        updateEpicStatus(epic.getId());
    }

    public void deleteEpicById(int id) {
        epics.remove(id);
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public Subtask createSubtask(Subtask subtask) {
        subtask.id = nextId++;
        subtasks.put(subtask.id, subtask);
        subtask.getEpic().addSubtask(subtask);
        return subtask;
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.id, subtask);
        updateEpicStatus(subtask.getEpic().getId());
    }

    public void deleteSubtaskById(int id) {
        subtasks.remove(id);
    }

    public List<Subtask> getSubtasksByEpicId(int epicId) {
        return getAllSubtasks().stream()
                .filter(subtask -> subtask.getEpic().getId() == epicId)
                .collect(Collectors.toList());
    }

    public void updateEpicStatus(int epicId) {
        Epic epic = (Epic) getTaskById(epicId);
        if (epic != null) {
            epic.setStatus(epic.getStatus());
        }
    }
}
