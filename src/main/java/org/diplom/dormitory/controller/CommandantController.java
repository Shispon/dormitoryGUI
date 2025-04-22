package org.diplom.dormitory.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;

public class CommandantController {
    @FXML private ComboBox<String> actionComboBox;

    @FXML
    private void handleAction() {
        String selectedAction = actionComboBox.getValue();

        if (selectedAction == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Выберите действие.");
            alert.showAndWait();
            return;
        }

        switch (selectedAction) {
            case "Создать нового пользователя":
                // Логика создания пользователя
                break;
            case "Найти пользователя":
                // Логика поиска пользователя
                break;
            case "Список присутствующих/отсутствующих":
                // Логика отображения списка
                break;
        }
    }
}