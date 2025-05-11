package org.diplom.dormitory.controller.commandant;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.application.Platform;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

// Импорт классов для запросов (можешь использовать RestTemplate, Retrofit или свой сервис)
import org.diplom.dormitory.model.GroupModel;
import org.diplom.dormitory.model.RoleModel;
import org.diplom.dormitory.service.ResidentApiService.CommandantService;

public class CreateStuffController {

    @FXML private TextField firstName;
    @FXML private TextField secondName;
    @FXML private TextField lastName;
    @FXML private TextField mail;
    @FXML private TextField phoneNumber;
    @FXML private TextField password;

    @FXML private CheckBox isPasswordGenerator;
    @FXML private ComboBox<RoleModel> roles;
    @FXML private ComboBox<GroupModel> groupComboBox;
    @FXML private Button saveButton;
    @FXML private Label requestLabel;

    private List<RoleModel> allRoles;
    private List<GroupModel> allGroups;

    @FXML
    public void initialize() {
        loadRoles();
        loadGroups();

        // Генерация пароля по нажатию на CheckBox
        isPasswordGenerator.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                String generated = generatePassword(10);
                password.setText(generated);
                password.setDisable(true); // запретить редактирование
            } else {
                password.clear();
                password.setDisable(false); // разрешить редактирование
            }
        });

        // Обработка выбора роли
        roles.valueProperty().addListener((obs, oldRole, newRole) -> {
            if (newRole != null && newRole.getRoleName().equalsIgnoreCase("Curator")) {
                groupComboBox.setVisible(true);
                groupComboBox.setManaged(true);
            } else {
                groupComboBox.setVisible(false);
                groupComboBox.setManaged(false);
            }
        });

        // Кнопка сохранить (упрощённо)
        saveButton.setOnAction(e -> handleSave());
    }

    // Загрузка ролей и фильтрация только нужных
    private void loadRoles() {
        // Представим, что ApiService.getRoles() возвращает список всех ролей
        allRoles = ApiService.getRoles(); // или await/async
        List<RoleModel> filtered = allRoles.stream()
                .filter(role -> role.getRoleName().equalsIgnoreCase("Curator") ||
                        role.getRoleName().equalsIgnoreCase("Commandant") ||
                        role.getRoleName().equalsIgnoreCase("Security") ||
                        role.getRoleName().equalsIgnoreCase("mentor"))
                .collect(Collectors.toList());
        roles.getItems().addAll(filtered);
    }

    // Загрузка всех групп
    private void loadGroups() {
        allGroups = (List<GroupModel>) CommandantService.createGroupLoadTask(); // подгрузка с сервера
        groupComboBox.getItems().addAll(allGroups);
    }

    // Генерация пароля из случайных символов
    private String generatePassword(int length) {
        String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(symbols.charAt(random.nextInt(symbols.length())));
        }
        return sb.toString();
    }

    // Обработка кнопки Сохранить
    private void handleSave() {
        String fname = firstName.getText();
        String sname = secondName.getText();
        String lname = lastName.getText();
        String mailText = mail.getText();
        String phone = phoneNumber.getText();
        String pass = password.getText();
        RoleModel selectedRole = roles.getValue();

        if (selectedRole == null) {
            requestLabel.setText("Пожалуйста, выберите роль.");
            return;
        }

        Integer groupId = null;
        if (selectedRole.getRoleName().equalsIgnoreCase("Куратор")) {
            GroupModel selectedGroup = groupComboBox.getValue();
            if (selectedGroup == null) {
                requestLabel.setText("Куратору нужно выбрать группу.");
                return;
            }
            groupId = selectedGroup.getId();
        }

        // Отправка данных на сервер
        boolean success = ApiService.saveUser(fname, sname, lname, mailText, phone, pass, selectedRole.getId(), groupId);

        if (success) {
            requestLabel.setText("Работник успешно создан!");
            clearForm();
        } else {
            requestLabel.setText("Ошибка при создании.");
        }
    }

    private void clearForm() {
        firstName.clear();
        secondName.clear();
        lastName.clear();
        mail.clear();
        phoneNumber.clear();
        password.clear();
        isPasswordGenerator.setSelected(false);
        roles.setValue(null);
        groupComboBox.setValue(null);
        groupComboBox.setVisible(false);
        groupComboBox.setManaged(false);
    }
}
