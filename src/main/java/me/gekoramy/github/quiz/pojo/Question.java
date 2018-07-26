package me.gekoramy.github.quiz.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Luca Mosetti
 */
public class Question implements Serializable {

    private String id;
    private String question;
    private List<String> answers;
    private int correct;

    public Question() {
    }

    public Question(String id, String question, List<String> answers, int correct) {
        this.id = id;
        this.question = question;
        this.answers = answers;
        this.correct = correct;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    @Override
    public String toString() {
        if (question == null || answers == null)
            return "NOT INSTANCED";

        StringBuilder result = new StringBuilder();

        result.append(id).append("\n").append(question).append("\n");

        for (int i = 0, size = answers.size(); i < size; i++) {
            result.append((char) ('A' + i)).append(") ").append(answers.get(i)).append("\n");
        }

        return result.toString();
    }
}
