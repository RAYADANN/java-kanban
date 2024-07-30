package com.yandex.sprint_4.service;

import com.yandex.sprint_4.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private static final Map<Integer, Node<Task>> taskIndex = new HashMap<>();
    private Node<Task> head = null;
    private Node<Task> tail = null;

    public void linkLast(Task task) {
        Node<Task> newNode = new Node<>(task);

        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> current = head;
        while (current != null) {
            tasks.add(current.taskMain);
            current = current.next;
        }
        return tasks;
    }

    public void removeNode(Node<Task> nodeToRemove) {
        if (nodeToRemove == null) {
            return;
        }

        if (nodeToRemove == head) {
            head = head.next;
        } else {
            nodeToRemove.next = nodeToRemove.prev;
        }
        if (nodeToRemove == tail) {
            tail = tail.prev;
        } else {
            nodeToRemove.prev = nodeToRemove.next;
        }
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (taskIndex.containsKey(task.getId())) {
            removeNode(taskIndex.get(task.getId()));
        }
        linkLast(task);
        taskIndex.put(task.getId(), tail);
    }

    @Override
    public List<Task> getHistory() {
        return new LinkedList<>(getTasks());
    }

    @Override
    public void remove(int id) {
        if (taskIndex.containsKey(id)) {
            removeNode(taskIndex.get(id));
        }
        taskIndex.remove(id);
    }
}
