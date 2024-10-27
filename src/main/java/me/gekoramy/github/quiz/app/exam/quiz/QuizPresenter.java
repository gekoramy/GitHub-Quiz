package me.gekoramy.github.quiz.app.exam.quiz;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import me.gekoramy.github.quiz.exception.NotLoggedException;
import me.gekoramy.github.quiz.pojo.Question;
import me.gekoramy.github.quiz.service.EditContent;
import me.gekoramy.github.quiz.util.GitHubClients;

import javax.inject.Inject;
import java.net.URL;
import java.util.*;

/**
 * @author Luca Mosetti
 */
public class QuizPresenter implements Initializable {
    @FXML
    private GridPane root;
    @FXML
    private TextArea txtQuestion;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnPeek;
    @FXML
    private Button btnClipboard;
    @FXML
    private VBox pnlAnswers;

    @Inject
    private Question question;
    @Inject
    private EditContent editContent;

    private static final Background CORRECT_BG = new Background(new BackgroundFill(Color.YELLOW, new CornerRadii(100), null));

    private final ClipboardContent content = new ClipboardContent();
    private final List<RadioButton> options = new ArrayList<>();
    private final ToggleGroup group = new ToggleGroup();
    private final Map<String, RadioButton> accelerators = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String txt = question.getQuestion();
        txtQuestion.setText(txt);

        for (String answer : question.getAnswers()) {
            RadioButton tmp = new RadioButton(answer);
            tmp.setWrapText(true);
            tmp.setToggleGroup(group);
            options.add(tmp);
            pnlAnswers.getChildren().add(tmp);
        }

        for (int i = 0; i < options.size(); i++) {
            putAssociation(i);
        }

        content.putString(question.toString());
        btnClipboard.setOnAction(e -> copyToClipboard());

        btnPeek.setOnMousePressed(e -> showCorrect());
        btnPeek.setOnMouseReleased(e -> hideCorrect());
        btnEdit.setOnAction(e -> onEdit());
        root.setOnKeyTyped(this::onKeyTyped);
    }

    private void onEdit() {
        if (!editContent.isRunning()) {
            try {
                editContent.setClient(GitHubClients.get());
                editContent.reset();
                editContent.start();
            } catch (NotLoggedException e) {
                e.printStackTrace();
            }
        }
    }

    private void putAssociation(int i) {
        accelerators.put(Integer.toString(i + 1), options.get(i));
        accelerators.put(Character.toString((char) ('A' + i)), options.get(i));
    }

    /**
     * Associate every answer with 2 string:
     * - (char) (A + index)
     * - (int) index
     *
     * @param event key event
     */
    private void onKeyTyped(KeyEvent event) {
        String typed = event.getCharacter().toUpperCase();
        if (accelerators.containsKey(typed))
            accelerators.get(typed).setSelected(true);
    }

    /**
     * Adds a {@link ChangeListener} which will be notified whenever the value
     * of the {@code ObservableValue} changes. If the same listener is added
     * more than once, then it will be notified more than once. That is, no
     * check is made to ensure uniqueness.
     * <p>
     * Note that the same actual {@code ChangeListener} instance may be safely
     * registered for different {@code ObservableValues}.
     * <p>
     *
     * @param listener The listener to register
     * @throws NullPointerException if the listener is null
     */
    public void addOnChosenAnswerListener(ChangeListener<? super Toggle> listener) {
        group.selectedToggleProperty().addListener(listener);
    }

    /**
     * @return true if the user has chosen an answer,
     * false otherwise
     */
    public boolean hasAnswered() {
        return group.getSelectedToggle() != null;
    }

    /**
     * @return index of the chosen answer, -1 otherwise
     */
    private int getChosenAnswer() {
        for (int i = 0; i < options.size(); i++) {
            RadioButton r = options.get(i);
            if (r.isSelected()) return i;
        }

        return -1;
    }

    /**
     * @return true if the user has chosen the right answer,
     * false otherwise
     */
    public boolean isCorrect() {
        return getChosenAnswer() == question.getCorrect();
    }

    /**
     * highlight the correct answer
     */
    public void showCorrect() {
        btnPeek.setText("!");
        options.get(question.getCorrect()).setBackground(CORRECT_BG);
    }

    /**
     * make all the answers look the same
     */
    private void hideCorrect() {
        btnPeek.setText("?");
        options.get(question.getCorrect()).setBackground(Background.EMPTY);
    }

    /**
     * permanently disable peek after answers
     */
    public void disablePeek() {
        btnPeek.setDisable(true);
        btnPeek.setText("?");
    }

    /**
     * copy to clipboard the question with the possible answers
     */
    private void copyToClipboard() {
        Clipboard.getSystemClipboard().setContent(content);
    }
}
