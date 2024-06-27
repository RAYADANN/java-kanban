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
        subtasks.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Epic createEpic(Epic epic) {
        epic.id = nextId++;
        epics.put(epic.id, epic);
        return epic;
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.id, epic);

    }

    public void deleteEpicById(int id) {
        epics.remove(id);
        for (Subtask sub : subtasks.values()) {
            if (sub.getEpic().id == id) {
                subtasks.remove(sub);
            }
        }
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
        subtask.id = nextId++;
        subtasks.put(subtask.id, subtask);
        subtask.getEpic().addSubtask(subtask);
        updateEpicStatus(subtask.getEpic().getId());
        return subtask;
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.id, subtask);
        updateEpicStatus(subtask.getEpic().getId());
    }

    public void deleteSubtaskById(int id) {
        Epic ep = subtasks.get(id).getEpic();
        ep.removeSubtask(subtasks.get(id));
        updateEpic(ep);
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
        if (epic.getSubtasks().stream().anyMatch(task -> task.getStatus().equals(Status.IN_PROGRESS))) {
            return Status.IN_PROGRESS;
        } else if (epic.getSubtasks().stream().allMatch(task -> task.getStatus().equals(Status.DONE))) {
            return Status.DONE;
        } else {
            return Status.NEW;
        }
    }
}
