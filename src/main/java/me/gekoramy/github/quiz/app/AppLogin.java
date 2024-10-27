package me.gekoramy.github.quiz.app;

import com.airhacks.afterburner.injection.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.gekoramy.github.quiz.app.login.LoginPresenter;
import me.gekoramy.github.quiz.app.login.LoginView;
import org.eclipse.egit.github.core.client.GitHubClient;

/**
 * @author Luca Mosetti
 */
public class AppLogin extends Application {

    private LoginPresenter presenter;

    public void start(Stage stage) {
        LoginView appView = new LoginView();
        presenter = (LoginPresenter) appView.getPresenter();
        Scene scene = new Scene(appView.getView());
        stage.setTitle("GitHub");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public GitHubClient getGitHubClient() {
        return presenter.getGitHubClient();
    }

    @Override
    public void stop() {
        Injector.forgetAll();
    }
}
