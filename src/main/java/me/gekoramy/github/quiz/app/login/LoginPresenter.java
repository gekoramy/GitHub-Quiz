package me.gekoramy.github.quiz.app.login;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
    private TextField txtUser;
    @FXML
    private PasswordField txtPass;
    @FXML
    private Button btnSubmit;

    private final Login login = new Login();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        login.usernameProperty().bind(txtUser.textProperty());
        login.passwordProperty().bind(txtPass.textProperty());

        login.setOnSucceeded(e -> {
            if (login.getValue() != null) {
                ((Stage) btnSubmit.getScene().getWindow()).close();
            }
        });

        pnlIncorrect.managedProperty().bind(pnlIncorrect.visibleProperty());
        pnlIncorrect.visibleProperty().bind(Bindings.and(
                login.valueProperty().isNull(),
                login.stateProperty().isEqualTo(new SimpleObjectProperty<>(Worker.State.SUCCEEDED))
        ));

        btnSubmit.disableProperty().bind(login.runningProperty());
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
