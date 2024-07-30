package com.yandex.sprint_4.service;

public class Node<T> {
    public T taskMain;
    public Node<T> next;
    public Node<T> prev;

    public Node(T task) {
        this.taskMain = task;
        this.next = null;
        this.prev = null;
    }
}
