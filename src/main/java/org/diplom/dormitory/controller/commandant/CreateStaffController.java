package org.diplom.dormitory.controller.commandant;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

// Импорт классов для запросов (можешь использовать RestTemplate, Retrofit или свой сервис)
import org.diplom.dormitory.model.GroupModel;
import org.diplom.dormitory.model.RoleModel;
import org.diplom.dormitory.model.StaffModel;
import org.diplom.dormitory.service.ResidentApiService.CommandantService;
import org.diplom.dormitory.util.JsonBuilder;

public class CreateStaffController {

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

        saveButton.setOnAction(e -> {
            try {
                handleSave();
            } catch (JsonProcessingException ex) {
                requestLabel.setText("Ошибка сериализации JSON");
                ex.printStackTrace(); // для отладки
            }
        });
    }

    private void loadRoles() {
        Task<ObservableList<RoleModel>> task = CommandantService.getAllRolesTask();
        task.setOnSucceeded(e -> {
            allRoles = task.getValue();
            List<RoleModel> filtered = allRoles.stream()
                    .filter(role -> role.getRoleName().equalsIgnoreCase("Curator") ||
                            role.getRoleName().equalsIgnoreCase("Commandant") ||
                            role.getRoleName().equalsIgnoreCase("Security") ||
                            role.getRoleName().equalsIgnoreCase("Mentor"))
                    .collect(Collectors.toList());
            roles.getItems().addAll(filtered);
        });
        new Thread(task).start();
    }


    private void loadGroups() {
        Task<ObservableList<GroupModel>> task = CommandantService.getAllGroupsTask();
        task.setOnSucceeded(e -> {
            allGroups = task.getValue();
            groupComboBox.getItems().addAll(allGroups);
        });
        new Thread(task).start();
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

    private void handleSave() throws JsonProcessingException {
        // Создаем новый объект StaffModel и заполняем его данными из полей
        StaffModel staffModel = new StaffModel();
        staffModel.setFirstName(firstName.getText());
        staffModel.setSecondName(secondName.getText());
        staffModel.setLastName(lastName.getText());
        staffModel.setEmail(mail.getText());
        staffModel.setPhoneNumber(phoneNumber.getText());
        staffModel.setPassword(password.getText());

        // Получаем выбранную роль
        RoleModel selectedRole = roles.getValue();
        if (selectedRole == null) {
            requestLabel.setText("Пожалуйста, выберите роль.");
            return;
        }

        // Устанавливаем роль в модель
        staffModel.setRoleId(selectedRole.getId());

        // Проверка: если выбран "Curator", обязательно должна быть выбрана группа
        final GroupModel selectedGroup = selectedRole.getRoleName().equalsIgnoreCase("Curator")
                ? groupComboBox.getValue()
                : null;

        if (selectedRole.getRoleName().equalsIgnoreCase("Curator") && selectedGroup == null) {
            requestLabel.setText("Куратору нужно выбрать группу.");
            return;
        }

        // Преобразуем модель в JSON
        String json = JsonBuilder.buildStaffJson(staffModel);

        // Создаем задачу для отправки данных на сервер
        Task<StaffModel> createTask = new Task<>() {
            @Override
            protected StaffModel call() {

                return CommandantService.createStaff(json);
            }
        };

        // Обработка успешного завершения задачи
        createTask.setOnSucceeded(e -> {
            StaffModel createdStaff = createTask.getValue();
            if (createdStaff != null && createdStaff.getId() != null) {
                requestLabel.setText("Работник успешно создан!");

                // Если это куратор — назначить его на группу
                if (selectedGroup != null) {
                    Task<Integer> assignTask = CommandantService.setCuratorInGroup(
                            selectedGroup.getId(),
                            createdStaff.getId()
                    );

                    assignTask.setOnSucceeded(ev -> {
                        if (assignTask.getValue() == 1) {
                            requestLabel.setText("Работник создан и назначен куратором группы.");
                        } else {
                            requestLabel.setText("Работник создан, но возникла ошибка при назначении куратора.");
                        }
                    });

                    new Thread(assignTask).start();
                }

                clearForm();
            } else {
                requestLabel.setText("Ошибка при создании работника.");
            }
        });

        // Запускаем задачу в отдельном потоке
        new Thread(createTask).start();
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
