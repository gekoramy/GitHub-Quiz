package me.gekoramy.github.quiz.service;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.util.Pair;
import me.gekoramy.github.quiz.persistance.QuestionsRetrieverOffline;
import me.gekoramy.github.quiz.records.Question;
import me.gekoramy.github.quiz.util.Pool;
import org.eclipse.egit.github.core.Repository;

import java.io.File;

/**
 * @author Luca Mosetti
 */
public class Read extends Service<Pair<Repository, Pool<Question>>> {

    private File source;

    public void setSource(File source) {
        this.source = source;
    }

    @Override
    protected Task<Pair<Repository, Pool<Question>>> createTask() {
        return new Task<>() {
            @Override
            protected Pair<Repository, Pool<Question>> call() throws Exception {
                return QuestionsRetrieverOffline.getInstance().read(source);
            }
        };
    }
}
