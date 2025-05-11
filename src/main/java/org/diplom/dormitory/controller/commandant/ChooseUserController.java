package org.diplom.dormitory.controller.commandant;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import org.diplom.dormitory.MainApp;

import java.io.IOException;


public class ChooseUserController {
    @FXML private Button returnBackButton;
    @FXML private MenuItem chooseUserMenuItem;
    @FXML private MenuItem chooseStaffMenuItem;
    @FXML private VBox dynamicContentPane;

    @FXML
    public void initialize() {
        returnBackButton.setOnAction(event -> {
            try {
                MainApp.showCommandantWindow();
            }catch (Exception e) {
                e.printStackTrace();
            }
        });
        chooseUserMenuItem.setOnAction(event -> {
            loadForm("/fxml/commandant/createUser.fxml");
        });
        chooseStaffMenuItem.setOnAction(event -> {
            loadForm("/fxml/commandant/createStaff.fxml");
        });

    }

    private void loadForm(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node content = loader.load();
            dynamicContentPane.getChildren().setAll(content); // вставляем форму
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
