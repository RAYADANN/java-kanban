package test;

import com.yandex.sprint_4.model.Status;
import com.yandex.sprint_4.model.Task;
import com.yandex.sprint_4.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    HistoryManager historyManager;
    Task task;
    Task task2;
    List<Task> history;

    @BeforeEach
    public void beforeEach() {
        historyManager = Managers.getDefaultHistory();

        task = new Task(0, "Задача 1", "Описание 1", Status.NEW, Duration.ofMinutes(5),
                LocalDateTime.now());
        historyManager.add(task);

        task2 = new Task(1, "Задача 2", "Описание 2", Status.NEW, Duration.ofMinutes(10),
                LocalDateTime.now().plusMinutes(10));
        historyManager.add(task2);

        history = historyManager.getHistory();
    }

    @Test
    public void testAddTask() {
        assertEquals(2, history.size());

        Task firstTask = history.get(task.getId());
        Task lastTask = history.getLast();
        assertEquals("Задача 1", firstTask.getName());
        assertEquals("Задача 2", lastTask.getName());

    }

    @Test
    public void testRemoveTask() {
        assertEquals(2, history.size());

        historyManager.remove(task.getId());
        history = historyManager.getHistory();
        assertEquals(1, history.size());
    }
}