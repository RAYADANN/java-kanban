package test;

import com.yandex.sprint_4.model.Status;
import com.yandex.sprint_4.model.Task;
import com.yandex.sprint_4.service.HistoryManager;
import com.yandex.sprint_4.service.InMemoryTaskManager;
import com.yandex.sprint_4.service.Managers;
import com.yandex.sprint_4.service.TaskManager;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    @Test
    public void testAddTaskKeepsPreviousVersion() {
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task = new Task(1,"Задача 1", "Описание 1", Status.NEW);
        historyManager.add(task);

        task.setDescription("Новое описание");
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());

        Task firstTask = history.getFirst();
        assertEquals("Задача 1", firstTask.getName());
        assertEquals("Описание 1", firstTask.getDescription());

    }
}