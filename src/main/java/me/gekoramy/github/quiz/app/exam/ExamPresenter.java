package me.gekoramy.github.quiz.app.exam;

import javafx.application.HostServices;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import me.gekoramy.github.quiz.app.exam.quiz.QuizPresenter;
import me.gekoramy.github.quiz.app.exam.quiz.QuizView;
import me.gekoramy.github.quiz.pojo.Question;
import me.gekoramy.github.quiz.service.EditContent;
import org.eclipse.egit.github.core.Repository;

import javax.inject.Inject;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Luca Mosetti
 */
public class ExamPresenter implements Initializable {
    @FXML
    private BorderPane root;
    @FXML
    private Hyperlink btnSizeDown;
    @FXML
    private Hyperlink btnSizeUp;
    @FXML
    private Hyperlink btnPrevious;
    @FXML
    private Hyperlink btnNext;
    @FXML
    private Button btnSubmit;
    @FXML
    private Button btnShow;
    @FXML
    private TabPane pnlQuestions;

    @Inject
    private List<Question> questions;
    @Inject
    private Repository repo;
    @Inject
    private HostServices hostServices;

    private static final int DEFAULT_FONT_SIZE = 13;
    private static final IntegerProperty fontSize = new SimpleIntegerProperty(DEFAULT_FONT_SIZE);
    private final List<QuizPresenter> quizzes = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int i = 0; i < questions.size(); i++) {
            createQuiz(i);
        }

        root.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString()));
        btnSizeDown.setOnAction(e -> fontSize.setValue(fontSize.getValue() - 1));
        btnSizeUp.setOnAction(e -> fontSize.setValue(fontSize.getValue() + 1));
        btnNext.setOnAction(e -> pnlQuestions.getSelectionModel().selectNext());
        btnPrevious.setOnAction(e -> pnlQuestions.getSelectionModel().selectPrevious());
        btnSubmit.setOnAction(e -> onSubmit());
        btnShow.setOnAction(e -> onShow());
    }

    public void setAccelerators(Scene scene) {
        ObservableMap<KeyCombination, Runnable> accelerators = scene.getAccelerators();

        accelerators.put(new KeyCodeCombination(KeyCode.MINUS, KeyCombination.SHORTCUT_DOWN), btnSizeDown::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.PLUS, KeyCombination.SHORTCUT_DOWN), btnSizeUp::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.DIGIT0, KeyCombination.SHORTCUT_DOWN), () -> fontSize.setValue(DEFAULT_FONT_SIZE));

        accelerators.put(new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.SHORTCUT_ANY), btnNext::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.DOWN, KeyCombination.SHORTCUT_ANY), btnNext::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_ANY), btnNext::fire);

        accelerators.put(new KeyCodeCombination(KeyCode.LEFT, KeyCombination.SHORTCUT_ANY), btnPrevious::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.UP, KeyCombination.SHORTCUT_ANY), btnPrevious::fire);
        accelerators.put(new KeyCodeCombination(KeyCode.P, KeyCombination.SHORTCUT_ANY), btnPrevious::fire);

        accelerators.put(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_ANY), btnSubmit::fire);
    }

    private void createQuiz(int i) {
        Map<Object, Object> customProperties = new HashMap<>();
        customProperties.put("question", questions.get(i));
        customProperties.put("editContent", new EditContent(hostServices, repo, questions.get(i).getId()));

        QuizView q = new QuizView(customProperties::get);
        QuizPresenter p = ((QuizPresenter) q.getPresenter());
        p.addOnChosenAnswerListener(((observable, oldValue, newValue) -> answered(i)));
        q.getViewAsync(v -> pnlQuestions.getTabs().add(
                i, new Tab(Integer.toString(i + 1), v)
        ));
        quizzes.add(p);
    }

    /**
     * shows the correct answers of every questions
     */
    private void onShow() {
        quizzes.forEach(quiz -> {
            quiz.showCorrect();
            quiz.disablePeek();
        });
        btnShow.setDisable(true);
    }

    /**
     * Shows a brief result of the test highlighting the uncorrected answers
     */
    private void onSubmit() {
        AtomicBoolean proceed = new AtomicBoolean(true);

        if (!quizzes.stream().allMatch(QuizPresenter::hasAnswered)) {
            Alert dialog = new Alert(Alert.AlertType.WARNING,
                    "You haven't answered to every question!\nDo you wish to proceed anyway?",
                    ButtonType.YES, ButtonType.NO);
            dialog.showAndWait().ifPresent(b -> proceed.set(b.equals(ButtonType.YES)));
        }


        if (proceed.get()) {
            Alert dialog = new Alert(Alert.AlertType.INFORMATION);
            long points = correct();
            dialog.setHeaderText("Correct answers: " + points + "/" + quizzes.size());
            dialog.setContentText("Your mark is " + Long.toString(points * 30 / quizzes.size()) + "/30");
            dialog.show();

            btnShow.setDisable(false);
        }
    }

    /**
     * highlights the corrected and uncorrected answers
     *
     * @return the count of all the corrected answers
     */
    private long correct() {
        int points = 0;

        for (int i = 0; i < quizzes.size(); i++) {
            if (quizzes.get(i).isCorrect()) {
                points++;
                pnlQuestions.getTabs().get(i).setStyle("-fx-background-color: #62C454;");
            } else {
                pnlQuestions.getTabs().get(i).setStyle("-fx-background-color: #EE6A5E;");
            }
        }

        return points;
    }

    /**
     * highlights the answered questions
     *
     * @param index question index
     */
    private void answered(int index) {
        pnlQuestions.getTabs().get(index).setStyle("-fx-background-color: #F7BE4F;");
    }
}
