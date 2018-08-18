package me.gekoramy.github.quiz.pojo;

import java.util.List;

/**
 * @author Luca Mosetti
 */
public class Data {

    private String question;
    private List<String> answers;
    private char correct;

    public Data() {
    }

    public Data(String question, List<String> answers, char correct) {
        this.question = question;
        this.answers = answers;
        this.correct = correct;
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

    public char getCorrect() {
        return correct;
    }

    public void setCorrect(char correct) {
        this.correct = correct;
    }
}
