package me.gekoramy.github.quiz.service;

import javafx.application.HostServices;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * @author Luca Mosetti
 */
public class OpenLink extends Service<Void> {

    private final String url;
    private final HostServices hostServices;

    public OpenLink(String url, HostServices hostServices) {
        this.url = url;
        this.hostServices = hostServices;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() {
                hostServices.showDocument(url);
                return null;
            }
        };
    }
}
