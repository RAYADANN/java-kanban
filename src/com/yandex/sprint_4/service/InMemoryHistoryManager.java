package com.yandex.sprint_4.service;

import com.yandex.sprint_4.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private static final Map<Integer, Node> taskIndex = new HashMap<>();
    private Node head = null;
    private Node tail = null;

    public void linkLast(Task task) {
        Node newNode = new Node(task);

        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
        }
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.getTaskMain());
            current = current.getNext();
        }
        return tasks;
    }

    public void removeNode(Node nodeToRemove) {
        if (nodeToRemove == null) {
            return;
        }

        if (nodeToRemove == head) {
            head = head.getNext();
        } else {
            nodeToRemove.setNext(nodeToRemove.getPrev());
        }
        if (nodeToRemove == tail) {
            tail = tail.getPrev();
        } else {
            nodeToRemove.setPrev(nodeToRemove.getNext());
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
