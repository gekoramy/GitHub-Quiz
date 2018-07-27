package me.gekoramy.github.quiz.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.gekoramy.github.quiz.app.home.HomeView;

/**
 * @author Luca Mosetti
 */
public class AppHome extends Application {
    @Override
    public void start(Stage stage) {
        HomeView appView = new HomeView();
        Scene scene = new Scene(appView.getView());
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String... args) {
        launch(args);
    }
}
