package me.gekoramy.github.quiz.service;

import javafx.application.HostServices;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;

/**
 * @author Luca Mosetti
 */
public class EditContent extends Service<Void> {

    private HostServices hostServices;
    private Repository repo;
    private String content;
    private GitHubClient client;

    public EditContent() {
    }

    public EditContent(HostServices hostServices, Repository repo, String content) {
        this.hostServices = hostServices;
        this.repo = repo;
        this.content = content;
    }

    public void setClient(GitHubClient client) {
        this.client = client;
    }

    @Override
    protected Task<Void> createTask() {
        assert client != null;
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                hostServices.showDocument(
                        repo.getHtmlUrl() + "/edit/" + repo.getMasterBranch() + "/" + content
                );
                return null;
            }
        };
    }
}
