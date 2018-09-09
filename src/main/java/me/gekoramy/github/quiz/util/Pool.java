package me.gekoramy.github.quiz.util;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.io.Serializable;
import java.util.ArrayList;
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
    private final DoubleProperty progress;

    public Pool(List<T> totalList) {
        this.totalList = totalList;
        this.toDoList = new LinkedList<>(totalList);
        this.progress = new SimpleDoubleProperty(1);
    }

    public Pool(List<T> totalList, LinkedList<T> toDoList) {
        this.totalList = totalList;
        this.toDoList = toDoList;
        toDoList.retainAll(totalList);
        this.progress = new SimpleDoubleProperty((double) toDo() / (double) total());
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

    public DoubleProperty progressProperty() {
        return progress;
    }

    public void revert() {
        toDoList.clear();
        toDoList.addAll(totalList);
        this.progress.set(1);
    }

    public List<T> retrieve(int many) {
        if (many > toDoList.size())
            throw new ArrayIndexOutOfBoundsException(toDoList.size());

        List<T> subList = IntStream.range(0, many).mapToObj(i -> toDoList.poll()).collect(Collectors.toList());

        this.progress.set((double) toDo() / (double) total());

        return subList;
    }

    public boolean isDone() {
        return toDoList.size() == 0;
    }

    public void shuffle() {
        Collections.shuffle(toDoList);
    }
}
