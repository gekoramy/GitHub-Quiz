package me.gekoramy.github.quiz.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.util.List;

/**
 * @author Luca Mosetti
 */
public class Repositories extends Service<List<Repository>> {

    private final GitHubClient client;

    public Repositories(GitHubClient client) {
        this.client = client;
    }

    @Override
    protected Task<List<Repository>> createTask() {
        return new Task<>() {
            @Override
            protected List<Repository> call() throws Exception {
                return new RepositoryService(client)
                        .getRepositories();
            }
        };
    }
}
