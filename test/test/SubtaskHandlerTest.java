package test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandex.sprint_4.model.Epic;
import com.yandex.sprint_4.model.Status;
import com.yandex.sprint_4.model.Subtask;
import com.yandex.sprint_4.service.BaseHttpHandler;
import com.yandex.sprint_4.service.HttpTaskServer;
import com.yandex.sprint_4.service.Managers;
import com.yandex.sprint_4.service.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SubTaskHandlerTest {
    TaskManager manager = Managers.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = BaseHttpHandler.gson;

    SubTaskHandlerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAllTasks();
        manager.deleteAllSubtasks();
        manager.deleteAllEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddSubTask() throws IOException, InterruptedException {
        Epic epicTask = new Epic(1,"Test 1", "Text", Status.NEW );
        manager.createEpic(epicTask);
        Subtask subTask = new Subtask(2,"Test 2", "Testing subtask 2",
                Status.NEW, epicTask, Duration.ofMinutes(20),  LocalDateTime.of(2014, Month.APRIL, 8, 12, 30) );
        String subtaskJson = gson.toJson(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Subtask> tasksFromManager = manager.getAllSubtasks();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasksFromManager.size());
        assertEquals("Test 2", tasksFromManager.get(0).getName());
    }

    @Test
    public void testGetAllSubTask() throws IOException, InterruptedException {
        Epic epicTask = new Epic(1,"Test 1", "Text", Status.NEW );
        manager.createEpic(epicTask);
        Subtask subTask = new Subtask(2,"Test 2", "Testing subtask 2",
                Status.NEW, epicTask, Duration.ofMinutes(20),  LocalDateTime.of(2014, Month.APRIL, 12, 12, 30) );
        Subtask subTask1 = new Subtask(3,"Test 3", "Testing subtask 3",
                Status.NEW, epicTask, Duration.ofMinutes(20),  LocalDateTime.of(2014, Month.APRIL, 13, 12, 30) );
        manager.createSubtask(subTask);
        manager.createSubtask(subTask1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        class SubTaskListTypetoken extends TypeToken<List<Subtask>> {
        }
        List<Subtask> tasksFromManager = gson.fromJson(response.body(), new SubTaskListTypetoken().getType());
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size());
        assertEquals("Test 2", tasksFromManager.get(0).getName());
        assertEquals("Test 3", tasksFromManager.get(1).getName());
    }

    @Test
    public void testGetSubTaskId() throws IOException, InterruptedException {
        Epic epicTask = new Epic(1,"Test 1", "Text", Status.NEW );
        manager.createEpic(epicTask);
        Subtask subTask = new Subtask(2,"Test 2", "Testing subtask 2",
                Status.NEW, epicTask, Duration.ofMinutes(20),  LocalDateTime.of(2014, Month.APRIL, 8, 12, 30) );
        Subtask acteaulTask = manager.createSubtask(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Subtask jsonTask = gson.fromJson(response.body(), Subtask.class);
        assertNotNull(jsonTask);
        assertEquals(acteaulTask, jsonTask);
    }

    @Test
    public void testUpdateSubTask() throws IOException, InterruptedException {
        Epic epicTask = new Epic(1,"Test 1", "Text", Status.NEW );
        manager.createEpic(epicTask);
        Subtask subTask = new Subtask(2,"Test 2", "Testing subtask 2",
                Status.NEW, epicTask, Duration.ofMinutes(20),  LocalDateTime.of(2014, Month.APRIL, 8, 12, 30) );
        manager.createSubtask(subTask);
        subTask.setName("Test 3");
        String updateTask = gson.toJson(manager.updateSubtask(subTask), Subtask.class);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).
                POST(HttpRequest.BodyPublishers.ofString(updateTask)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertNotEquals("Test 2", manager.getSubtaskById(2).getName(), "Задача не обновилась");
    }

    @Test
    public void testDeleteSubTask() throws IOException, InterruptedException {
        Epic epicTask = new Epic(1,"Test 1", "Text", Status.NEW );
        manager.createEpic(epicTask);
        Subtask subTask = new Subtask(2,"Test 2", "Testing subtask 2",
                Status.NEW, epicTask, Duration.ofMinutes(20),  LocalDateTime.of(2014, Month.APRIL, 8, 12, 30) );
        manager.createSubtask(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Subtask sub = manager.getSubtaskById(2);
        assertNull(sub);
    }
}