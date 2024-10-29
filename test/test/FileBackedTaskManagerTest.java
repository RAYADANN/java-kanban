package test;

import com.yandex.sprint_4.model.*;
import com.yandex.sprint_4.service.FileBackedTaskManager;
import com.yandex.sprint_4.service.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    private File tempFile;
    private FileBackedTaskManager taskManager;
    Task task1;
    Epic epic1;
    Subtask subtask1;

    @BeforeEach
    void setUp() {
        tempFile = new File("src/com/yandex/sprint_4/resources/Data.csv");
        taskManager = (FileBackedTaskManager) Managers.getDefault();
        task1 = new Task(1, "Task 1", "Description for Task 1", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2014, Month.APRIL, 8, 12, 30));
        epic1 = new Epic(2, "Epic 1", "Description for Epic 1", Status.NEW);
        subtask1 = new Subtask(3, "Subtask 1", "Description for Subtask 1", Status.NEW,
                epic1, Duration.ofMinutes(10), LocalDateTime.of(2014, Month.APRIL, 8, 12, 45));
    }

    @Test
    void testSaveEmptyFile() {
        taskManager.save();

        assertTrue(tempFile.exists());

        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
            String line = reader.readLine();
            assertEquals("id;type;name;status;description;duration;timeStart;epicID", line);
            assertNull(reader.readLine());
        } catch (IOException e) {
            fail("Ошибка чтения файла: " + e.getMessage());
        }
    }

    @Test
    void testSaveMultipleTasks() {
        taskManager.createTask(task1);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);

        taskManager.save();

        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
            String line = reader.readLine(); // Skip header
            assertEquals("1;TASK;Task 1;NEW;Description for Task 1;PT10M;2014-04-08T12:30;", reader.readLine());
            assertEquals("2;EPIC;Epic 1;NEW;Description for Epic 1;PT10M;2014-04-08T12:45;", reader.readLine());
            assertEquals("3;SUB;Subtask 1;NEW;Description for Subtask 1;PT10M;2014-04-08T12:45;2",
                    reader.readLine());
            assertNull(reader.readLine()); // Проверяем, что больше строк нет
        } catch (IOException e) {
            fail("Ошибка чтения файла: " + e.getMessage());
        }
    }

    @Test
    void testLoadMultipleTasks() {
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("id;type;name;status;description;duration;timeStart;epicID\n");
            writer.write("1;TASK;Task 1;NEW;Description for Task 1;" + Duration.ofMinutes(10) + ";"
                    + LocalDateTime.of(2014, Month.APRIL, 8, 12, 30) + "\n");
            writer.write("2;EPIC;Epic 1;IN_PROGRESS;Description for Epic 1;\n");
            writer.write("3;SUB;Subtask 1;NEW;Description for Subtask 1;" + Duration.ofMinutes(10) + ";"
                    + LocalDateTime.of(2014, Month.APRIL, 8, 12, 45) + ";2\n");
        } catch (IOException e) {
            fail("Ошибка записи в файл: " + e.getMessage());
        }

        FileBackedTaskManager.loadFromFile(tempFile);

        List<String> dataTask = taskManager.getDataTask();
        List<Task> tasks = new ArrayList<>();
        for (String t : dataTask) {
            tasks.add(taskManager.fromString(t));
        }

        assertEquals(3, tasks.size());
        assertEquals("Task 1", tasks.get(0).getName());

        List<Epic> allEpics = taskManager.getAllEpics();
        assertEquals(1, allEpics.size());
        assertEquals("Epic 1", allEpics.get(0).getName());

        List<Subtask> allSubtasks = taskManager.getAllSubtasks();
        assertEquals(1, allSubtasks.size());
        assertEquals("Subtask 1", allSubtasks.get(0).getName());
    }
}