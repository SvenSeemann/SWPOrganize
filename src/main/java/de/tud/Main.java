package de.tud;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Scene overviewScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Organisation des Softwaretechnologie Projekts");

        this.overviewScene = new Scene(root, 900, 1024);
        primaryStage.setScene(this.overviewScene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
