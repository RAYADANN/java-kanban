package test;

import com.yandex.sprint_4.model.Epic;
import com.yandex.sprint_4.model.Status;
import com.yandex.sprint_4.model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    Epic epic;
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    void beforeEach() {
        epic = new Epic(1, "Название эпика", "Описание эпика", Status.NEW);
        subtask1 = new Subtask(101, "Подзадача 1", "Описание подзадачи 1", Status.DONE, epic);
        subtask2 = new Subtask(102, "Подзадача 2", "Описание подзадачи 2", Status.IN_PROGRESS, epic);
    }

    @Test
    void testGetSubtasks() {
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        List<Subtask> subtasks = epic.getSubtasks();

        assertEquals(2, subtasks.size());
        assertTrue(subtasks.contains(subtask1));
        assertTrue(subtasks.contains(subtask2));
    }

    @Test
    void testAddSubtask() {
        epic.addSubtask(subtask1);

        assertEquals(1, epic.getSubtasks().size());
        assertTrue(epic.getSubtasks().contains(subtask1));
    }

    @Test
    void testRemoveSubtask() {
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        epic.removeSubtask(subtask1);

        assertEquals(1, epic.getSubtasks().size());
        assertFalse(epic.getSubtasks().contains(subtask1));
        assertTrue(epic.getSubtasks().contains(subtask2));
    }

    @Test
    void testRemoveAllSubtasks() {
        epic.addSubtask(subtask1);
        epic.addSubtask(subtask2);

        epic.removeAllSubtasks();

        assertTrue(epic.getSubtasks().isEmpty());
    }
}