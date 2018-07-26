package me.gekoramy.github.quiz.service;


import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

/**
 * @author Luca Mosetti
 */
public class UpdateAvailable extends Service<Boolean> {

    private final Repository repo;
    private GitHubClient client;

    public UpdateAvailable(Repository repo) {
        this.repo = repo;
    }

    public Repository getRepo() {
        return repo;
    }

    public void setClient(GitHubClient client) {
        this.client = client;
    }

    @Override
    protected Task<Boolean> createTask() {
        assert client != null;

        return new Task<Boolean>() {
            @Override
            protected Boolean call() {
                try {
                    RepositoryService service = new RepositoryService(client);
                    return service.getRepository(repo).getUpdatedAt().after(repo.getUpdatedAt());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            }
        };
    }
}
