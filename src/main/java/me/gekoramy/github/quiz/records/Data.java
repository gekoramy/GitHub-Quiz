package me.gekoramy.github.quiz.records;

import java.util.List;

/**
 * @author Luca Mosetti
 */
public record Data(
    String question,
    List<String> answers,
    Character correct
) {
    @SuppressWarnings("unused")
    public Data() {
        this(null, null, null);
    }
}
