package test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandex.sprint_4.model.Status;
import com.yandex.sprint_4.model.Task;
import com.yandex.sprint_4.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TaskHandlerTest {
    TaskManager manager = Managers.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = BaseHttpHandler.gson;

    TaskHandlerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAllTasks();
        manager.deleteAllEpics();
        manager.deleteAllSubtasks();
        taskServer.start();
    }
    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task(1, "Test 2", "Testing task 2",
                Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2014, Month.APRIL, 8, 12, 30));
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = manager.getAllTasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetAllTask() throws IOException, InterruptedException {
        Task task1 = new Task(1, "Задача 1", "Описание задачи 1", Status.NEW, Duration.ofMinutes(5),
                LocalDateTime.now());
        Task task2 = new Task(2, "Задача 2", "Описание задачи 2", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(10));
        manager.createTask(task1);
        manager.createTask(task2);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        class TaskListTypetoken extends TypeToken<List<Task>> {
        }
        List<Task> tasksFromManager = gson.fromJson(response.body(), new TaskListTypetoken().getType());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Задача 1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
        assertEquals("Задача 2", tasksFromManager.get(1).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTaskId() throws IOException, InterruptedException {
        Task task = new Task(1, "Test 2", "Testing task 2",
                Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2014, Month.APRIL, 8, 12, 30));
        Task acteaulTask = manager.createTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Task jsonTask = gson.fromJson(response.body(), Task.class);
        assertNotNull(jsonTask);
        assertEquals(acteaulTask, jsonTask);
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task(1, "Test 2", "Testing task 2",
                Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2014, Month.APRIL, 8, 12, 30));
        manager.createTask(task);
        task.setName("Test 1");
        String updateTask = gson.toJson(manager.updateTask(task), Task.class);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).
                POST(HttpRequest.BodyPublishers.ofString(updateTask)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertNotEquals("Test 2", manager.getTaskById(1).getName(), "Задача не обновилась");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task(1, "Test 2", "Testing task 2",
                Status.NEW, Duration.ofMinutes(30), LocalDateTime.of(2014, Month.APRIL, 8, 12, 30));
        manager.createTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertNull(manager.getTaskById(1), "Задача не удалилась");
    }
}
