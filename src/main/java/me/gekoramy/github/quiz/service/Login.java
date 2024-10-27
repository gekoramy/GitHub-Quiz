package me.gekoramy.github.quiz.service;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;

/**
 * @author Luca Mosetti
 */
public class Login extends Service<GitHubClient> {
    private final StringProperty token = new SimpleStringProperty();

    public StringProperty tokenProperty() {
        return token;
    }

    @Override
    protected Task<GitHubClient> createTask() {
        return new Task<>() {
            @Override
            protected GitHubClient call() {
                try {
                    GitHubClient client = new GitHubClient();
                    client.setOAuth2Token(token.get());
                    new UserService(client).getEmails();
                    return client;
                } catch (Exception e) {
                    return null;
                }
            }
        };
    }
}
