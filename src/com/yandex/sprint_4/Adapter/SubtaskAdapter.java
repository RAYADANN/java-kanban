package com.yandex.sprint_4.Adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.yandex.sprint_4.model.Epic;
import com.yandex.sprint_4.model.Status;
import com.yandex.sprint_4.model.Subtask;
import com.yandex.sprint_4.model.TaskTypes;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SubtaskAdapter extends TypeAdapter<Subtask> {
    protected static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void write(JsonWriter jsonWriter, Subtask subtask) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("id").value(subtask.getId());
        jsonWriter.name("taskType").value(subtask.getTaskType().toString());
        jsonWriter.name("name").value(subtask.getName());
        jsonWriter.name("description").value(subtask.getDescription());
        jsonWriter.name("status").value(subtask.getStatus().toString());
        jsonWriter.name("duration").value(subtask.getDuration().toMinutes());
        jsonWriter.name("startTime").value(subtask.getStartTime().format(TIME_FORMATTER));
        jsonWriter.name("epic").beginObject();
        jsonWriter.name("id").value(subtask.getEpic().getId());
        jsonWriter.name("taskType").value(subtask.getEpic().getTaskType().toString());
        jsonWriter.name("name").value(subtask.getEpic().getName());
        jsonWriter.name("description").value(subtask.getEpic().getDescription());
        jsonWriter.name("status").value(subtask.getEpic().getStatus().toString());
        jsonWriter.endObject();
        jsonWriter.endObject();
    }

    @Override
    public Subtask read(JsonReader jsonReader) throws IOException {
        Integer id = null;
        String name = null;
        String description = null;
        Status status = null;
        TaskTypes taskType = null;
        LocalDateTime startTime = null;
        Duration duration = null;
        Integer epicId = null;
        String epicName = null;
        String epicDescription = null;
        Status epicStatus = null;
        LocalDateTime epicStartTime = null;
        Duration epicDuration = null;
        Epic epic;
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            switch (jsonReader.nextName()) {
                case "name":
                    name = jsonReader.nextString();
                    break;
                case "description":
                    description = jsonReader.nextString();
                    break;
                case "id":
                    try {
                        id = jsonReader.nextInt();
                    } catch (Exception e) {
                        id = null;
                    }
                    break;
                case "status":
                    status = switch (jsonReader.nextString()) {
                        case "IN_PROGRESS" -> Status.IN_PROGRESS;
                        case "DONE" -> Status.DONE;
                        default -> Status.NEW;
                    };
                    break;
                case "taskType":
                    jsonReader.nextString();
                    break;
                case "startTime":
                    startTime = LocalDateTime.parse(jsonReader.nextString(), TIME_FORMATTER);
                    break;
                case "duration":
                    duration = Duration.ofMinutes(jsonReader.nextInt());
                    break;
                case "epic":
                    jsonReader.beginObject();
                    while (jsonReader.hasNext()) {
                        String field = jsonReader.nextName();

                        if (field.equals("id")) {
                            try {
                                epicId = jsonReader.nextInt();
                            } catch (Exception ex) {
                                epicId = null;
                            }

                        } else if (field.equals("name")) {
                            epicName = jsonReader.nextString();

                        } else if (field.equals("description")) {
                            epicDescription = jsonReader.nextString();

                        } else if (field.equals("status")) {
                            epicStatus = switch (jsonReader.nextString()) {
                                case "IN_PROGRESS" -> Status.IN_PROGRESS;
                                case "DONE" -> Status.DONE;
                                default -> Status.NEW;
                            };

                        } else if (field.equals("duration")) {
                            try {
                                epicDuration = Duration.ofMinutes(jsonReader.nextInt());
                            } catch (Exception e) {
                                duration = null;
                            }
                        } else if (field.equals("startTime")) {
                            epicStartTime = LocalDateTime.parse(jsonReader.nextString());
                        } else {
                            jsonReader.skipValue();
                        }
                    }
                    jsonReader.endObject();
            }
        }
        jsonReader.endObject();
        if (!Objects.isNull(id)) {
            epic = new Epic(epicId, epicName, epicDescription, epicStatus);
            epic.addSubtask(new Subtask(id, name, description, status,  epic, duration, startTime ));
            return new Subtask(id, name, description, status,  epic, duration, startTime );
        } else {
            epic = new Epic(epicId, epicName, epicDescription, epicStatus);
            return new Subtask(id, name, description, status, epic, duration, startTime);
        }
    }
}
