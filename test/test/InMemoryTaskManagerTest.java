package test;

import com.yandex.sprint_4.model.Epic;
import com.yandex.sprint_4.model.Status;
import com.yandex.sprint_4.model.Subtask;
import com.yandex.sprint_4.model.Task;
import com.yandex.sprint_4.service.InMemoryTaskManager;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    private final InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    void testCreateAndRetrieveTask() {
        Task task = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.now().plusMinutes(10));
        taskManager.createTask(task);
        int taskId = task.getId();
        Task retrievedTask = taskManager.getTaskById(taskId);
        assertNotNull(retrievedTask);
        assertEquals(task, retrievedTask);
    }

    @Test
    void testCreateAndRetrieveEpic() {
        Epic epic = new Epic(1, "Эпик 1", "Описание эпика 1", Status.NEW);
        taskManager.createEpic(epic);
        int epicId = epic.getId();
        Epic retrievedEpic = taskManager.getEpicById(epicId);
        assertNotNull(retrievedEpic);
        assertEquals(epic, retrievedEpic);
    }

    @Test
    void testCreateAndRetrieveSubtask() {
        Epic epic = new Epic(1, "Эпик 2", "Описание эпика 2", Status.NEW);
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask(2, "Подзадача 1", "Описание подзадачи 1", Status.NEW, epic,
                Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(10));
        taskManager.createSubtask(subtask);

        int subtaskId = subtask.getId();
        Subtask retrievedSubtask = taskManager.getSubtaskById(subtaskId);
        assertNotNull(retrievedSubtask);
        assertEquals(subtask, retrievedSubtask);
    }

    @Test
    void testIdCollision() {
        Task task1 = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW, Duration.ofMinutes(5),
                LocalDateTime.now());
        taskManager.createTask(task1);

        Task task2 = new Task(2, "Задача 2", "Описание задачи 2", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(10));
        taskManager.createTask(task2);
        task2.setId(1);

        assertEquals(2, taskManager.getAllTasks().size());
        assertEquals(task1, taskManager.getTaskById(1));
    }

    @Test
    void testTaskImmutabilityOnCreate() {
        Task originalTask = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(10));
        originalTask.setId(1);
        taskManager.createTask(originalTask);
        Task addedTask = originalTask;

        assertEquals(originalTask.getId(), addedTask.getId());
        assertEquals(originalTask.getName(), addedTask.getName());
        assertEquals(originalTask.getDescription(), addedTask.getDescription());
        assertEquals(originalTask.getStatus(), addedTask.getStatus());


    }

}