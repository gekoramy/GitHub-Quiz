package me.gekoramy.github.quiz.app;

import com.airhacks.afterburner.injection.Injector;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.gekoramy.github.quiz.app.exam.ExamPresenter;
import me.gekoramy.github.quiz.app.exam.ExamView;
import me.gekoramy.github.quiz.pojo.Question;
import me.gekoramy.github.quiz.service.OpenLink;
import org.eclipse.egit.github.core.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Luca Mosetti
 */
public class AppExam extends Application {

    private final Map<Object, Object> customProperties = new HashMap<>();

    public AppExam() {
    }

    public AppExam(Repository repo, List<Question> questions) {
        customProperties.put("questions", questions);
        customProperties.put("repo", repo);
        customProperties.put("hostServices", getHostServices());
    }

    @Override
    public void start(Stage stage) {
        Injector.setConfigurationSource(customProperties::get);
        ExamView appView = new ExamView();
        Scene scene = new Scene(appView.getView());
        ((ExamPresenter) appView.getPresenter()).setAccelerators(scene);
        stage.setTitle("Exam");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        Injector.forgetAll();
    }

}
