package me.gekoramy.github.quiz.app.login;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import me.gekoramy.github.quiz.service.Login;
import org.eclipse.egit.github.core.client.GitHubClient;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Luca Mosetti
 */
public class LoginPresenter implements Initializable {
    @FXML
    private StackPane pnlIncorrect;
    @FXML
    private PasswordField txtPass;
    @FXML
    private Button btnSubmit;

    private final Login login = new Login();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login.tokenProperty().bind(txtPass.textProperty());

        login.setOnSucceeded(e -> {
            if (login.getValue() == null) {
                pnlIncorrect.setVisible(true);
            } else {
                ((Stage) btnSubmit.getScene().getWindow()).close();
            }
        });

        pnlIncorrect.managedProperty().bind(pnlIncorrect.visibleProperty());

        btnSubmit.disableProperty().bind(Bindings.or(
            txtPass.textProperty().isEmpty(),
            login.runningProperty()
        ));
        btnSubmit.setOnAction(e -> validate());
    }

    public GitHubClient getGitHubClient() {
        return login.getValue();
    }

    private void validate() {
        if (!login.isRunning()) {
            login.reset();
            login.start();
        }
    }
}
