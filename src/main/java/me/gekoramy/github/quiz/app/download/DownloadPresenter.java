package me.gekoramy.github.quiz.app.download;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import me.gekoramy.github.quiz.app.AppPreview;
import me.gekoramy.github.quiz.service.Download;
import me.gekoramy.github.quiz.service.Store;
import me.gekoramy.github.quiz.util.Constants;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Luca Mosetti
 */
public class DownloadPresenter implements Initializable {
    @FXML
    private StackPane root;
    @FXML
    private ProgressBar progress;
    @FXML
    private Label lblRepo;

    @Inject
    private Download download;
    @Inject
    private Store store;

    private final FileChooser fileChooser;

    public DownloadPresenter() {
        this.fileChooser = new FileChooser();
        this.fileChooser.getExtensionFilters().add(Constants.QUESTIONS_FILE);
        this.fileChooser.setTitle("Store to quiz yourself offline");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fileChooser.setInitialFileName(download.getRepo().getName().replaceFirst(Constants.PREFIX, ""));

        lblRepo.setText(download.getRepo().getName());
        progress.progressProperty().bind(download.progressProperty());
        download.setOnSucceeded(e -> onSucceeded());
        download.setOnFailed(e -> onFailed());
        download.start();
    }

    private void onFailed() {
        progress.setVisible(false);
        lblRepo.setText(download.getException().getMessage());
    }

    private void onSucceeded() {
        store.setOutput(fileChooser.showSaveDialog(null));
        store.setQuestionPool(download.getValue().getValue());
        store.start();

        Platform.runLater(() -> {
            AppPreview app = new AppPreview(download.getValue().getKey(), download.getValue().getValue());
            app.start((Stage) root.getScene().getWindow());
        });
    }
}
