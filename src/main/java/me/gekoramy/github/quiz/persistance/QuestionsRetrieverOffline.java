package me.gekoramy.github.quiz.persistance;

import javafx.util.Pair;
import me.gekoramy.github.quiz.pojo.Question;
import me.gekoramy.github.quiz.util.Pool;
import org.eclipse.egit.github.core.Repository;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Luca Mosetti
 */
@SuppressWarnings("unchecked")
public class QuestionsRetrieverOffline {
    private static final QuestionsRetrieverOffline instance = new QuestionsRetrieverOffline();

    public static QuestionsRetrieverOffline getInstance() {
        return instance;
    }

    private QuestionsRetrieverOffline() { }

    public File store(File output, Repository repo, Pool<Question> questionPool) throws IOException {

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(output))) {
            oos.writeObject(repo);
            oos.writeObject(questionPool.getTotalList());
            oos.writeObject(questionPool.getToDoList());
        }

        return output;
    }

    public Pair<Repository, Pool<Question>> read(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return new Pair<>((Repository) ois.readObject(), new Pool<>((List<Question>) ois.readObject(), (LinkedList<Question>) ois.readObject()));
        }
    }
}
