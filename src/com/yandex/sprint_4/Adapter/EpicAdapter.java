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
import java.util.Objects;

public class EpicAdapter extends TypeAdapter<Epic> {
    @Override
    public void write(JsonWriter jsonWriter, Epic epic) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("name").value(epic.getName());
        jsonWriter.name("description").value(epic.getDescription());
        jsonWriter.name("id").value(epic.getId());
        jsonWriter.name("status").value(epic.getStatus().toString());
        jsonWriter.name("taskType").value(epic.getTaskType().toString());
        if (epic.getDuration() == null) {
            jsonWriter.name("duration").value("null");
        } else {
            jsonWriter.name("duration").value(epic.getDuration().toMinutes());
        }
        if (epic.getStartTime() == null) {
            jsonWriter.name("startTime").value("null");
        } else {
            jsonWriter.name("startTime").value(epic.getStartTime().format(SubtaskAdapter.TIME_FORMATTER));
        }
        if (epic.getEndTime() == null) {
            jsonWriter.name("endTime").value("null");
        } else {
            jsonWriter.name("endTime").value(epic.getEndTime().format(SubtaskAdapter.TIME_FORMATTER));
        }
        jsonWriter.name("subTasks").beginArray();
        for (Subtask subTask : epic.getSubtasks()) {
            jsonWriter.beginObject();
            jsonWriter.name("name").value(subTask.getName());
            jsonWriter.name("description").value(subTask.getDescription());
            jsonWriter.name("id").value(subTask.getId());
            jsonWriter.name("status").value(subTask.getStatus().toString());
            jsonWriter.name("taskType").value(subTask.getTaskType().toString());
            jsonWriter.name("duration").value(subTask.getDuration().toMinutes());
            jsonWriter.name("startTime").value(subTask.getStartTime().format(SubtaskAdapter.TIME_FORMATTER));
            jsonWriter.endObject();
        }
        jsonWriter.endArray();
        jsonWriter.endObject();
    }

    @Override
    public Epic read(JsonReader jsonReader) throws IOException {
        Integer id = null;
        String name = null;
        String description = null;
        Status status = null;
        LocalDateTime startTime = null;
        Duration duration = null;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String field = jsonReader.nextName();

            if (field.equals("id")) {
                try {
                    id = jsonReader.nextInt();
                } catch (Exception ex) {
                    id = null;
                }

            } else if (field.equals("name")) {
                name = jsonReader.nextString();

            } else if (field.equals("description")) {
                description = jsonReader.nextString();

            } else if (field.equals("status")) {
                status = switch (jsonReader.nextString()) {
                    case "IN_PROGRESS" -> Status.IN_PROGRESS;
                    case "DONE" -> Status.DONE;
                    default -> Status.NEW;
                };

            } else if (field.equals("duration")) {
                try {
                    duration = Duration.ofMinutes(jsonReader.nextInt());
                } catch (Exception e) {
                    duration = null;
                }
            } else if (field.equals("startTime")) {
                startTime = LocalDateTime.parse(jsonReader.nextString());
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();

        if (Objects.isNull(id) || id == 0) {
            return new Epic(id, name, description, status);
        } else {
            return new Epic(id, name, description, status);
        }
    }
}
