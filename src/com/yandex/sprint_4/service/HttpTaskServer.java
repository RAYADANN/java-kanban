package com.yandex.sprint_4.service;

import com.sun.net.httpserver.HttpServer;
import com.yandex.sprint_4.handler.*;
import com.yandex.sprint_4.model.Epic;
import com.yandex.sprint_4.model.Status;
import com.yandex.sprint_4.model.Subtask;
import com.yandex.sprint_4.model.Task;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        HistoryManager historyManager = Managers.getDefaultHistory();
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(historyManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public void start() {
        httpServer.start();
        System.out.println("Server started");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Server stopped");
    }

    public static void main(String[] args) throws IOException {
        try {
            FileBackedTaskManager.loadFromFile(new File("src/com/yandex/sprint_4/resources/Data.csv"));
        } catch (Exception ignored) {
            System.out.println(ignored.getMessage());
        }

        FileBackedTaskManager taskManager = (FileBackedTaskManager) Managers.getDefault();
        taskManager.save();
        if (taskManager.getDataTask() != null) {
            for (String line : taskManager.getDataTask()) {
                taskManager.fromString(line);
            }
        }

        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();
    }
}
