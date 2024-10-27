package me.gekoramy.github.quiz;

import javafx.application.Application;
import me.gekoramy.github.quiz.app.AppHome;

public interface Main {

    static void main(String[] args) {
        Application.launch(AppHome.class, args);
    }

}
