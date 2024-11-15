package com.yandex.sprint_4.handler;

import com.sun.net.httpserver.HttpExchange;
import com.yandex.sprint_4.service.Managers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public enum Endpoint {
    GET,
    GET_ALL,
    ADD,
    UPDATE,
    DELETE,
    GET_EPIC_SUBTASK,
    UNKNOWN,
}
