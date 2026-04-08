package com.jade.dreamjournalv2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DreamJournalApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DreamJournalApplication.class.getResource("vault-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Dream Journal");
        stage.setScene(scene);
        // Initialize the database before showing the UI
        DatabaseManager.initializeDatabase();
        stage.setMaximized(true);
        stage.show();
    }
}
