package test;

import com.yandex.sprint_4.model.Epic;
import com.yandex.sprint_4.model.Status;
import com.yandex.sprint_4.model.Subtask;
import com.yandex.sprint_4.model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    public void testEquals() {
        Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW);
        Task task2 = new Task(1, "Task 2", "Description 2", Status.IN_PROGRESS);

        assertEquals(task1, task2, "Tasks should be equal if their ids are the same.");
    }

    @Test
    public void testNotEquals() {
        Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2", Status.IN_PROGRESS);

        assertNotEquals(task1, task2, "Tasks should not be equal if their ids are different.");
    }

    @Test
    public void testEqualsWithSubclasses() {
        Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW);
        Epic epic = new Epic(1, "Subtask 2", "Description 2", Status.NEW);

        assertEquals(task1, epic, "Task и Epic равны друг другу так как у них одинаковый id");
    }

    @Test
    public void testNotEqualsWithSubclasses() {
        Task task1 = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW);
        Epic epic = new Epic(2, "Подзадача 2", "Описание подзадачи 2", Status.NEW);

        assertNotEquals(task1, epic, "Task и Epic не могут быть равны так как у них разные id");
    }

    @Test
    public void testEpicCannotAddSelfAsSubtask() {
        Epic epic = new Epic(1, "Эпик 1", "Описание эпика", Status.NEW);

        // не могу завершить этот тест так как у меня вообще нету возможности добавить эпик в эпик
        // Проверьте, что объект Subtask нельзя сделать своим же эпиком (Тоже не получится реализовать данный тест)
        //assertFalse(epic.addSubtask(epic), " Epic нельзя добавить в самого себя в виде подзадачи");
    }
}