package org.mmck.fxui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("main_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 760, 640);

        scene.getStylesheets().add(Objects.requireNonNull(
                MainApp.class.getResource("styles/dark-theme.css")).toExternalForm());

        stage.setTitle("mmck · Gestor de Mods");
        stage.setMinWidth(520);
        stage.setMinHeight(480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}