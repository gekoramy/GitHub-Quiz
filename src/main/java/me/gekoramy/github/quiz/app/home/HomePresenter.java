package me.gekoramy.github.quiz.app.home;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import me.gekoramy.github.quiz.app.AppPreview;
import me.gekoramy.github.quiz.app.AppRepositories;
import me.gekoramy.github.quiz.exception.NotLoggedException;
import me.gekoramy.github.quiz.service.Read;
import me.gekoramy.github.quiz.util.Constants;
import me.gekoramy.github.quiz.util.GitHubClients;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Luca Mosetti
 */
public class HomePresenter implements Initializable {

    @FXML
    private GridPane root;
    @FXML
    private Hyperlink btnAdd;
    @FXML
    private Hyperlink btnOpen;

    private final Read read = new Read();
    private final FileChooser fileChooser;

    public HomePresenter() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(Constants.QUESTIONS_FILE);
        fileChooser.setTitle("Choose a questions file");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnAdd.setOnAction(e -> onAdd());

        btnOpen.setOnAction(e -> onOpen());
        btnOpen.disableProperty().bind(read.runningProperty());

        read.setOnSucceeded(e -> onOpenSucceeded());

        root.setOnDragOver(this::onDragOver);
        root.setOnDragDropped(this::onDragDropped);
    }

    private void onDragOver(DragEvent event) {
        if (event.getGestureSource() != root && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    private void onDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles() && !read.isRunning()) {
            read.setSource(db.getFiles().iterator().next());
            read.reset();
            read.start();
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }

    private void onOpenSucceeded() {
        new AppPreview(read.getValue().getKey(), read.getValue().getValue()).start(new Stage());
    }

    private void onAdd() {
        try {
            new AppRepositories(GitHubClients.get()).start(new Stage());
        } catch (NotLoggedException e) {
            e.printStackTrace();
        }
    }

    private void onOpen() {
        if (!read.isRunning()) {
            read.setSource(fileChooser.showOpenDialog(null));
            read.reset();
            read.start();
        }
    }
}
