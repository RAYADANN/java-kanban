package test;

import com.yandex.sprint_4.model.Status;
import com.yandex.sprint_4.model.Task;
import com.yandex.sprint_4.service.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    @Test
    public void testAddTaskKeepsPreviousVersion() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task = new Task(0,"Задача 1", "Описание 1", Status.NEW);
        historyManager.add(new Task(task.getId(), task.getName(), task.getDescription(), task.getStatus()));

        task.setDescription("Новое описание");
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());

        Task firstTask = history.get(task.getId());
        assertEquals("Задача 1", firstTask.getName());
        assertEquals("Описание 1", firstTask.getDescription());

    }
}