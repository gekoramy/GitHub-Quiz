package me.gekoramy.github.quiz.app.repositories;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import me.gekoramy.github.quiz.app.AppDownload;
import me.gekoramy.github.quiz.exception.NotLoggedException;
import me.gekoramy.github.quiz.service.Repositories;
import me.gekoramy.github.quiz.util.Constants;
import me.gekoramy.github.quiz.util.GitHubClients;
import org.eclipse.egit.github.core.Repository;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Luca Mosetti
 */
public class RepositoriesPresenter implements Initializable {

    @FXML
    private ProgressIndicator progress;
    @FXML
    private TextField txtFilter;
    @FXML
    private ListView<Hyperlink> pnlRepositories;

    @Inject
    private Repositories repositories;

    private final ObservableList<Hyperlink> links = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repositories.start();
        repositories.setOnSucceeded(e -> onSucceeded());
        FilteredList<Hyperlink> filteredList = new FilteredList<>(links, data -> true);
        pnlRepositories.setItems(filteredList);
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> filteredList.setPredicate(data -> newValue == null || newValue.isEmpty() || data.getText().contains(newValue.toLowerCase())));
        progress.visibleProperty().bind(repositories.runningProperty());
    }

    private void onSucceeded() {
        List<Repository> questionsRepos = repositories.getValue().stream().filter(r -> r.getName().startsWith(Constants.PREFIX)).toList();

        if (questionsRepos.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "None of your repository starts with '%s'".formatted(Constants.PREFIX)).show();
        } else {
            questionsRepos.forEach(r -> {
                Hyperlink link = new Hyperlink(r.getName());
                link.setOnAction(e -> onChosen(r));
                links.add(link);
            });
        }
    }

    private void onChosen(Repository chosen) {
        try {
            new AppDownload(GitHubClients.get(), chosen).start(new Stage());
        } catch (NotLoggedException e) {
            e.printStackTrace();
        }
    }
}
