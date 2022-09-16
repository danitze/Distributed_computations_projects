package com.example.lab1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LabApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LabApplication.class.getResource("hello-view.fxml"));
        Parent root = fxmlLoader.load();
        MainController controller = fxmlLoader.getController();
        Scene scene = new Scene(root, 320, 240);
        stage.setTitle("Lab 1");
        stage.setOnHidden(e -> controller.onClose());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}