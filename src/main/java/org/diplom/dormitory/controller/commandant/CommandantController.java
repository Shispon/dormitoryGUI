package org.diplom.dormitory.controller.commandant;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import org.diplom.dormitory.MainApp;

import java.io.IOException;

public class CommandantController {
   @FXML private MenuItem propetryMenuItem;
   @FXML private MenuItem exitMenuItem;
   @FXML private MenuItem createNewUserMenuItem;
   @FXML private MenuItem deleteUserMenuItem;
   @FXML private MenuItem findUserMenuItem;
   @FXML private MenuItem createMessageMenuItem;
    @FXML private VBox dynamicContentPane;

   @FXML
   public void initialize() {
       exitMenuItem.setOnAction(event -> {
           try {
               MainApp.showLogin();
           } catch (Exception e) {
               throw new RuntimeException(e);
           }
       });
       createNewUserMenuItem.setOnAction(event -> {
           loadForm("/fxml/commandant/chooseUser.fxml");
       });
       findUserMenuItem.setOnAction(event -> {
           loadForm("/fxml/commandant/chooseList.fxml");
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
