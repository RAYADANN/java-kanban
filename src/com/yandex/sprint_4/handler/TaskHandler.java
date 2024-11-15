package com.yandex.sprint_4.handler;

import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.sprint_4.Adapter.DurationAdapter;
import com.yandex.sprint_4.Adapter.LocalDateTimeAdapter;
import com.yandex.sprint_4.model.Subtask;
import com.yandex.sprint_4.model.Task;
import com.yandex.sprint_4.service.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.lang.Character.getType;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final FileBackedTaskManager taskManager;

    public TaskHandler(TaskManager taskManager){
        this.taskManager = (FileBackedTaskManager) taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String requestMethod = exchange.getRequestMethod();
            String requestPath = exchange.getRequestURI().getPath();
            Endpoint endpoint = getEndpoint(requestPath, requestMethod);
            String text;
            int id;
            switch (endpoint) {
                case GET_ALL:
                    text = gson.toJson(taskManager.getAllTasks());
                    sendText(exchange, text);
                    break;
                case GET:
                    Optional<Integer> getId = getPostId(exchange);
                    if (getId.isEmpty()) {
                        sendNotFound(exchange);
                    }
                    id = getId.get();
                    Task task = taskManager.getTaskById(id);
                    if (task != null) {
                        text = gson.toJson(task, Task.class);
                        sendText(exchange, text);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case ADD:
                    String bodyTask = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Task newTask = gson.fromJson(bodyTask, Task.class);
                    if (!taskManager.getTaskIntersection(newTask)) {
                        sendHasInteractions(exchange);
                    } else {
                        taskManager.createTask(newTask);
                        text = "Задача добавлена";
                        sendPost(exchange, text);
                    }
                    break;
                case UPDATE:
                    String bodyUpdateTask = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Task updateTask = taskManager.updateTask(gson.fromJson(bodyUpdateTask, Task.class));
                    if (updateTask != null) {
                        if (taskManager.getTaskIntersection(updateTask)) {
                            sendHasInteractions(exchange);
                        } else {
                            text = "Задача обновлена";
                            sendPost(exchange, text);
                        }
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case DELETE:
                    Optional<Integer> deleteId = getPostId(exchange);
                    if (deleteId.isEmpty()) {
                        sendNotFound(exchange);
                    }
                    id = deleteId.get();
                    Task deleteTask = taskManager.getTaskById(id);
                    taskManager.deleteTaskById(id);
                    if (deleteTask != null) {
                        sendDelete(exchange);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case UNKNOWN:
                    sendNotEndpoint(exchange);
            }
        } catch (Exception e) {
            sendInternalServerError(exchange);
        }
    }
    private Optional<Integer> getPostId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }
}
