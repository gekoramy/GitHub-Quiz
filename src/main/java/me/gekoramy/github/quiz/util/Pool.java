package me.gekoramy.github.quiz.util;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.Serializable;
import java.util.*;
import java.util.stream.IntStream;

/**
 * @author Luca Mosetti
 */
public class Pool<T> implements Serializable {

    private final List<T> totalList;
    private final Queue<T> toDoList;
    private final IntegerProperty todoProperty;

    public Pool(List<T> totalList) {
        this.totalList = List.copyOf(totalList);
        this.toDoList = new ArrayDeque<>(totalList);
        this.todoProperty = new SimpleIntegerProperty(toDoList.size());
    }

    public Pool(List<T> totalList, List<T> toDoList) {
        this.totalList = List.copyOf(totalList);
        this.toDoList = new ArrayDeque<>(totalList.size());
        this.toDoList.addAll(toDoList);
        this.todoProperty = new SimpleIntegerProperty(toDo());
    }

    public List<T> getTotalList() {
        return totalList;
    }

    public List<T> getToDoList() {
        return List.copyOf(toDoList);
    }

    public int total() {
        return totalList.size();
    }

    public int toDo() {
        return toDoList.size();
    }

    public ReadOnlyIntegerProperty todoProperty() {
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

        List<T> subList = IntStream.range(0, many).mapToObj(i -> toDoList.poll()).toList();

        this.todoProperty.set(toDo());

        return subList;
    }

    public boolean isDone() {
        return toDoList.isEmpty();
    }

    public void shuffle() {
        ArrayList<T> tmp = new ArrayList<>(toDoList);
        Collections.shuffle(tmp);
        toDoList.clear();
        toDoList.addAll(tmp);
    }
}
