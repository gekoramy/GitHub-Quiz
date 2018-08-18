package me.gekoramy.github.quiz.app;

import com.airhacks.afterburner.injection.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.gekoramy.github.quiz.app.repositories.RepositoriesView;
import me.gekoramy.github.quiz.service.Repositories;
import org.eclipse.egit.github.core.client.GitHubClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Luca Mosetti
 */
public class AppRepositories extends Application {

    private final Map<Object, Object> customProperties = new HashMap<>();

    public AppRepositories() {
    }

    public AppRepositories(GitHubClient client) {
        customProperties.put("repositories", new Repositories(client));
    }

    @Override
    public void start(Stage stage) {
        Injector.setConfigurationSource(customProperties::get);
        RepositoriesView appView = new RepositoriesView();
        Scene scene = new Scene(appView.getView());
        stage.setHeight(370.0);
        stage.setTitle("Repositories");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        Injector.forgetAll();
    }
}
