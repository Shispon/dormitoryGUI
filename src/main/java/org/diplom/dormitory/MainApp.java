package org.diplom.dormitory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private static Stage primaryStage;

    public static void showLogin() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/login.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Авторизация");
        primaryStage.setFullScreen(false);
        primaryStage.show();
    }

    public static void showCommandantWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/commandant/commander.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Комендант");
            primaryStage.setFullScreen(true); // полноэкранный режим

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showChooseUserController() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/commandant/chooseUser.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Комендант");
            primaryStage.setFullScreen(true); // полноэкранный режим

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showLogin();
    }

    public static void chooseWindow(String role) {
        switch (role) {
            case "Commandant":
                MainApp.showCommandantWindow();
        }
    }
}
