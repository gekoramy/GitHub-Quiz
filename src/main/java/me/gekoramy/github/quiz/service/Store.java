package me.gekoramy.github.quiz.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import me.gekoramy.github.quiz.persistance.QuestionsRetrieverOffline;
import me.gekoramy.github.quiz.records.Question;
import me.gekoramy.github.quiz.util.Pool;
import org.eclipse.egit.github.core.Repository;

import java.io.File;

/**
 * @author Luca Mosetti
 */
public class Store extends Service<File> {

    private final Repository repo;
    private Pool<Question> questionPool;
    private File output;

    public Store(Repository repo) {
        assert repo != null;
        this.repo = repo;
    }

    public void setQuestionPool(Pool<Question> questionPool) {
        this.questionPool = questionPool;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    @Override
    protected Task<File> createTask() {
        assert questionPool != null && output != null;
        return new Task<>() {
            @Override
            protected File call() throws Exception {
                return QuestionsRetrieverOffline.getInstance().store(output, repo, questionPool);
            }
        };
    }
}
