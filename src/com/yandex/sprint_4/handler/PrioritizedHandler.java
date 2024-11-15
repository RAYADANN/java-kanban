package com.yandex.sprint_4.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.sprint_4.service.BaseHttpHandler;
import com.yandex.sprint_4.service.FileBackedTaskManager;
import com.yandex.sprint_4.service.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private FileBackedTaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = (FileBackedTaskManager) taskManager;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equals("GET")) {
                String text = gson.toJson(taskManager.getPrioritizedTasks());
                sendText(exchange, text);
            } else {
                sendNotEndpoint(exchange);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendInternalServerError(exchange);
        }
    }
}
