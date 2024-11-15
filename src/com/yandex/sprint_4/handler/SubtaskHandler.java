package com.yandex.sprint_4.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.sprint_4.model.Subtask;
import com.yandex.sprint_4.service.BaseHttpHandler;
import com.yandex.sprint_4.service.FileBackedTaskManager;
import com.yandex.sprint_4.service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private FileBackedTaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
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
                    text = gson.toJson(taskManager.getAllSubtasks());
                    sendText(exchange, text);
                    break;
                case GET:
                    Optional<Integer> getId = getPostId(exchange);
                    if (getId.isEmpty()) {
                        sendNotFound(exchange);
                    }
                    id = getId.get();
                    Subtask subTask = taskManager.getSubtaskById(id);
                    if (subTask != null) {
                        text = gson.toJson(subTask, Subtask.class);
                        sendText(exchange, text);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case ADD:
                    String bodyTask = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Subtask newTask = gson.fromJson(bodyTask, Subtask.class);
                    if (!taskManager.getTaskIntersection(newTask)) {
                        sendHasInteractions(exchange);
                    } else {
                        taskManager.createSubtask(newTask);
                        text = "Задача добавлена";
                        sendPost(exchange, text);
                    }
                    break;
                case UPDATE:
                    String bodyUpdateTask = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

                    Subtask updateTask = taskManager.updateSubtask(gson.fromJson(bodyUpdateTask, Subtask.class));
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

                    Subtask remove = taskManager.getSubtaskById(id);
                    taskManager.deleteSubtaskById(id);
                    if (remove != null) {
                        sendDelete(exchange);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case UNKNOWN:
                    sendNotEndpoint(exchange);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
