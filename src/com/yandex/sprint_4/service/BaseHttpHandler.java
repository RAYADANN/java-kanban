package com.yandex.sprint_4.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.yandex.sprint_4.Adapter.DurationAdapter;
import com.yandex.sprint_4.Adapter.EpicAdapter;
import com.yandex.sprint_4.Adapter.LocalDateTimeAdapter;
import com.yandex.sprint_4.Adapter.SubtaskAdapter;
import com.yandex.sprint_4.handler.Endpoint;
import com.yandex.sprint_4.handler.TaskHandler;
import com.yandex.sprint_4.model.Epic;
import com.yandex.sprint_4.model.Subtask;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class BaseHttpHandler {

    public static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Subtask.class, new SubtaskAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(Epic.class, new EpicAdapter())
            .create();

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendPost(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(201, 0);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        String text = "объект не был найден";
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(404, 0);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        String text = "задача пересекается с уже существующими";
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(406, 0);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendInternalServerError(HttpExchange exchange) throws IOException {
        String text = "произошла ошибка при обработке запроса";
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(500, 0);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendDelete(HttpExchange exchange) throws IOException {
        String text = "Задача удалена";
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, 0);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendNotEndpoint(HttpExchange exchange) throws IOException {
        String text = "такого эндпоинта не сущетствует";
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(404, 0);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected static Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (requestMethod.equals("GET") && pathParts.length < 3) {
            return Endpoint.GET_ALL;
        } else if (requestMethod.equals("GET") && pathParts.length == 3) {
            return Endpoint.GET;
        } else if (requestMethod.equals("GET") && pathParts.length == 4) {
            return Endpoint.GET_EPIC_SUBTASK;
        } else if (requestMethod.equals("POST") && pathParts.length < 3) {
            return Endpoint.ADD;
        } else if (requestMethod.equals("POST") && pathParts.length == 3) {
            return Endpoint.UPDATE;
        } else if (requestMethod.equals("DELETE") && pathParts.length == 3) {
            return Endpoint.DELETE;
        } else {
            return Endpoint.UNKNOWN;
        }
    }
}
