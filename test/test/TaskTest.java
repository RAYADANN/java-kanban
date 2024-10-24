package test;

import com.yandex.sprint_4.model.Epic;
import com.yandex.sprint_4.model.Status;
import com.yandex.sprint_4.model.Subtask;
import com.yandex.sprint_4.model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    public void testEquals() {
        Task task1 = new Task(1, "Задача 1", "Описание 1", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        Task task2 = new Task(1, "Задача 2", "Описание 2", Status.IN_PROGRESS,
                Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(10));

        assertEquals(task1, task2, "Задачи должны быть равными, если их идентификаторы одинаковы.");
    }

    @Test
    public void testNotEquals() {
        Task task1 = new Task(1, "Задача 1", "Описание 1", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        Task task2 = new Task(2, "Задача 2", "Описание 2", Status.IN_PROGRESS,
                Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(10));

        assertNotEquals(task1, task2, "Задачи не должны быть равными, если их идентификаторы разные.");
    }

    @Test
    public void testEqualsWithSubclasses() {
        Task task1 = new Task(1, "Задача 1", "Описание 1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(10));
        Epic epic = new Epic(1, "Эпик 2", "Описание 2", Status.NEW);

        assertEquals(task1, epic, "Task и Epic равны друг другу так как у них одинаковый id");
    }

    @Test
    public void testNotEqualsWithSubclasses() {
        Task task1 = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(10));
        Epic epic = new Epic(2, "Эпик 2", "Описание подзадачи 2", Status.NEW);

        assertNotEquals(task1, epic, "Task и Epic не могут быть равны так как у них разные id");
    }

}