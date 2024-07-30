package test;

import com.yandex.sprint_4.model.Status;
import com.yandex.sprint_4.model.Task;
import com.yandex.sprint_4.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    @Test
    public void testAddTask() {

        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task = new Task(0,"Задача 1", "Описание 1", Status.NEW);
        historyManager.add(task);

        Task task2 = new Task(1,"Задача 2", "Описание 2", Status.NEW);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());

        Task firstTask = history.get(task.getId());
        Task lastTask = history.getLast();
        assertEquals("Задача 1", firstTask.getName());
        assertEquals("Задача 2", lastTask.getName());

    }

    @Test
    public void testRemoveTask(){
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task = new Task(1,"Задача 1", "Описание 1", Status.NEW);
        historyManager.add(task);

        Task task2 = new Task(2,"Задача 2", "Описание 2", Status.NEW);
        historyManager.add(task2);
        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());

        historyManager.remove(task.getId());
        history = historyManager.getHistory();
        assertEquals(1, history.size());
    }
}