package me.gekoramy.github.quiz.util;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Luca Mosetti
 */
public class Pool<T> implements Serializable {

    private final List<T> totalList;
    private final LinkedList<T> toDoList;
    private final DoubleProperty todoProperty;

    public Pool(List<T> totalList) {
        this.totalList = totalList;
        this.toDoList = new LinkedList<>(totalList);
        this.todoProperty = new SimpleDoubleProperty(toDoList.size());
    }

    public Pool(List<T> totalList, LinkedList<T> toDoList) {
        this.totalList = totalList;
        this.toDoList = toDoList;
        toDoList.retainAll(totalList);
        this.todoProperty = new SimpleDoubleProperty(toDo());
    }

    public List<T> getTotalList() {
        return totalList;
    }

    public LinkedList<T> getToDoList() {
        return toDoList;
    }

    public int total() {
        return totalList.size();
    }

    public int toDo() {
        return toDoList.size();
    }

    public DoubleProperty todoProperty() {
        return todoProperty;
    }

    public void revert() {
        toDoList.clear();
        toDoList.addAll(totalList);
        this.todoProperty.set(toDo());
    }

    public List<T> retrieve(int many) {
        if (many > toDoList.size())
            throw new ArrayIndexOutOfBoundsException(toDoList.size());

        List<T> subList = IntStream.range(0, many).mapToObj(i -> toDoList.poll()).collect(Collectors.toList());

        this.todoProperty.set(toDo());

        return subList;
    }

    public boolean isDone() {
        return toDoList.size() == 0;
    }

    public void shuffle() {
        Collections.shuffle(toDoList);
    }
}
