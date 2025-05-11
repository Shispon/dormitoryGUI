package org.diplom.dormitory.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.diplom.dormitory.MainApp;
import org.diplom.dormitory.service.ApiService;


public class LoginController {
    @FXML private TextField mailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void onLogin() {
        String mail = mailField.getText();
        String password = passwordField.getText();

        String role = ApiService.authenticate(mail, password);
        if (role == null) {
            errorLabel.setText("Неверный логин или пароль.");
        } else {
            MainApp.chooseWindow(role); // переключение экрана
        }
    }
}
