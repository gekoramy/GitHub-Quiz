package me.gekoramy.github.quiz.service;


import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import me.gekoramy.github.quiz.app.AppExam;
import me.gekoramy.github.quiz.pojo.Question;
import me.gekoramy.github.quiz.util.Pool;
import org.eclipse.egit.github.core.Repository;

/**
 * @author Luca Mosetti
 */
public class ExamStarter extends Service<Void> {

    private final Repository repo;
    private final Pool<Question> questionPool;
    private int many = 12;

    public ExamStarter(Repository repo, Pool<Question> questionPool) {
        this.repo = repo;
        this.questionPool = questionPool;
    }

    public Pool<Question> getQuestionPool() {
        return questionPool;
    }

    public int getMany() {
        return many;
    }

    public void setMany(int many) {
        this.many = many;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() {
                if (questionPool.isDone()) questionPool.revert();
                Platform.runLater(() -> new AppExam(repo, questionPool.retrieve(Math.min(questionPool.toDo(), many))).start(new Stage()));
                return null;
            }
        };
    }

    public void shuffle() {
        questionPool.shuffle();
    }
}
