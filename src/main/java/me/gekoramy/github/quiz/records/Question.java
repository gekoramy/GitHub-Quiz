package me.gekoramy.github.quiz.records;

import java.io.Serializable;
import java.util.List;

/**
 * @author Luca Mosetti
 */
public record Question(
    String id,
    String question,
    List<String> answers,
    Integer correct
) implements Serializable {
    @SuppressWarnings("unused")
    public Question() {
        this(null, null, null, null);
    }
}
