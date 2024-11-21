package org.example.gestion_psicologia;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.gestion_psicologia.util.JPAUtil;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Gestión Psicología");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Limpia los recursos de JPA cuando la aplicación se cierre
        JPAUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}