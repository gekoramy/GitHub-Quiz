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
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    @Override
    protected Task<GitHubClient> createTask() {
        return new Task<GitHubClient>() {
            @Override
            protected GitHubClient call() {
                try {
                    GitHubClient client = new GitHubClient();
                    client.setCredentials(username.get(), password.get());
                    new UserService(client).getEmails();
                    return client;
                } catch (Exception e) {
                    return null;
                }
            }
        };
    }
}
