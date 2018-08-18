package me.gekoramy.github.quiz.app;

import com.airhacks.afterburner.injection.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.gekoramy.github.quiz.app.preview.PreviewPresenter;
import me.gekoramy.github.quiz.app.preview.PreviewView;
import me.gekoramy.github.quiz.pojo.Question;
import me.gekoramy.github.quiz.service.ExamStarter;
import me.gekoramy.github.quiz.service.OpenLink;
import me.gekoramy.github.quiz.service.UpdateAvailable;
import me.gekoramy.github.quiz.util.Pool;
import org.eclipse.egit.github.core.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Luca Mosetti
 */
public class AppPreview extends Application {

    private final Map<Object, Object> customProperties = new HashMap<>();

    public AppPreview() {
    }

    public AppPreview(Repository repository, Pool<Question> questionPool) {
        customProperties.put("updateAvailable", new UpdateAvailable(repository));
        customProperties.put("examStarter", new ExamStarter(repository, questionPool));
        customProperties.put("openLink", new OpenLink(repository.getHtmlUrl(), getHostServices()));
    }

    @Override
    public void start(Stage stage) {
        Injector.setConfigurationSource(customProperties::get);
        PreviewView appView = new PreviewView();
        Scene scene = new Scene(appView.getView());
        ((PreviewPresenter) appView.getPresenter()).setAccelerators(scene);
        stage.setHeight(150.0);
        stage.setTitle("Preview");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() {
        Injector.forgetAll();
    }
}
