package com.yandex.sprint_4.service;

import com.yandex.sprint_4.model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private static final List<String> dataTask = new ArrayList<>();
    private static final String DATA_FILE_PATH = "src/com/yandex/sprint_4/resources/Data.csv";
    private static final File HOME_FILE = new File(DATA_FILE_PATH);

    public FileBackedTaskManager() {
        try {
            HOME_FILE.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        List<Task> data = new ArrayList<>(super.getAllTasks());
        data.addAll(super.getAllEpics());
        data.addAll(super.getAllSubtasks());
        try (Writer writer = new FileWriter(HOME_FILE, false)) {
            writer.write("id;type;name;status;description;duration;timeStart;epicID\n");
            for (Task t : data) {
                writer.write(toString(t));
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String toString(Task task) {
        return String.format("%s;%s;%s;%s;%s;%s;%s;%s\n",
                task.getId(),
                task.getTaskType(),
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                task.getDuration(),
                task.getStartTime(),
                task.getEpic() != null ? task.getEpic().getId() : "");
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        try (Reader reader = new FileReader(file);
             BufferedReader br = new BufferedReader(reader)) {
            br.readLine();
            while (br.ready()) {
                String line = br.readLine();
                dataTask.add(line);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Task fromString(String value) {
        String[] data = value.split(";");
        switch (data[1]) {
            case "TASK":
                Task task = new Task(Integer.parseInt(data[0]),
                        data[2],
                        data[4],
                        data[3].equals("NEW") ? Status.NEW : data[3].equals("DONE") ? Status.DONE
                                : Status.IN_PROGRESS,
                        Duration.parse(data[5]),
                        LocalDateTime.parse(data[6]));
                createTask(task);
                return task;
            case "EPIC":
                Epic epic = (new Epic(Integer.parseInt(data[0]),
                        data[2],
                        data[4],
                        data[3].equals("NEW") ? Status.NEW : data[3].equals("DONE") ? Status.DONE
                                : Status.IN_PROGRESS));
                createEpic(epic);
                return epic;
            case "SUB":
                Subtask sub = (new Subtask(
                        Integer.parseInt(data[0]),
                        data[2],
                        data[4],
                        data[3].equals("NEW") ? Status.NEW : data[3].equals("DONE") ? Status.DONE
                                : Status.IN_PROGRESS,
                        getEpicById(Integer.parseInt(data[7])),
                        Duration.parse(data[5]),
                        LocalDateTime.parse(data[6])
                ));
                createSubtask(sub);
                return sub;

            default:
                return null;


        }
    }

    @Override
    public List<Task> getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();

    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();

    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    public List<String> getDataTask() {
        return dataTask;
    }

}
