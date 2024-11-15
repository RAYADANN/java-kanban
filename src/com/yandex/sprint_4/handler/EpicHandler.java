package com.yandex.sprint_4.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.sprint_4.model.Epic;
import com.yandex.sprint_4.service.BaseHttpHandler;
import com.yandex.sprint_4.service.FileBackedTaskManager;
import com.yandex.sprint_4.service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private FileBackedTaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
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
                    text = gson.toJson(taskManager.getAllEpics());
                    sendText(exchange, text);
                    break;
                case GET:
                    Optional<Integer> getId = getPostId(exchange);
                    if (getId.isEmpty()) {
                        sendNotFound(exchange);
                    }
                    id = getId.get();
                    Epic epicTask = taskManager.getEpicById(id);
                    if (epicTask != null) {
                        text = gson.toJson(epicTask, Epic.class);
                        sendText(exchange, text);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case GET_EPIC_SUBTASK:
                    Optional<Integer> getEpicId = getPostId(exchange);
                    if (getEpicId.isEmpty()) {
                        sendNotFound(exchange);
                    }
                    id = getEpicId.get();
                    Epic epicTask1 = taskManager.getEpicById(id);
                    if (epicTask1 != null) {
                        text = gson.toJson(taskManager.getSubtasksByEpicId(epicTask1.getId()));
                        sendText(exchange, text);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case ADD:
                    String bodyTask = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Epic newTask = gson.fromJson(bodyTask, Epic.class);
                    taskManager.createEpic(newTask);
                    text = "Задача добавлена";
                    sendPost(exchange, text);
                    break;
                case UPDATE:
                    String bodyUpdateTask = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Epic updateTask = taskManager.updateEpic(gson.fromJson(bodyUpdateTask, Epic.class));
                    if (updateTask != null) {
                        text = "Задача обновлена";
                        sendPost(exchange, text);
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
                    Epic remove = taskManager.getEpicById(id);
                    taskManager.deleteEpicById(id);
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
