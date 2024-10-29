package com.yandex.sprint_4.service;

import com.yandex.sprint_4.model.Task;

public class Node {
    private final Task taskMain;
    private Node next;
    private Node prev;

    public Node(Task task) {
        this.taskMain = task;
        this.next = null;
        this.prev = null;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Task getTaskMain() {
        return taskMain;
    }
}
