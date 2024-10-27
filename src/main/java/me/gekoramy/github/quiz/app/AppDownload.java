package me.gekoramy.github.quiz.app;

import com.airhacks.afterburner.injection.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.gekoramy.github.quiz.app.download.DownloadView;
import me.gekoramy.github.quiz.service.Download;
import me.gekoramy.github.quiz.service.Store;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Luca Mosetti
 */
public class AppDownload extends Application {

    private final Map<Object, Object> customProperties = new HashMap<>();

    @SuppressWarnings("unused")
    public AppDownload() {
    }

    public AppDownload(GitHubClient client, Repository repo) {
        customProperties.put("download", new Download(client, repo));
        customProperties.put("store", new Store(repo));
    }

    @Override
    public void start(Stage stage) {
        Injector.setConfigurationSource(customProperties::get);
        DownloadView appView = new DownloadView();
        Scene scene = new Scene(appView.getView());
        stage.setMinHeight(150.0);
        stage.setMaxHeight(150.0);
        stage.setTitle("Download");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        Injector.forgetAll();
    }
}
