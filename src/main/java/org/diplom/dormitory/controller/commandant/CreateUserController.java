package org.diplom.dormitory.controller.commandant;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.diplom.dormitory.model.GroupModel;
import org.diplom.dormitory.model.ResidentModel;
import org.diplom.dormitory.service.ResidentApiService.CommandantService;
import org.diplom.dormitory.util.JsonBuilder;

import java.io.IOException;

public class CreateUserController {

    @FXML
    private TextField firstName;
    @FXML
    private TextField secondName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField age;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField email;
    @FXML
    private TextField telegramId;
    @FXML
    private Button addPhotoButton;
    @FXML
    private ComboBox<GroupModel> groups ;
    @FXML
    private Button saveButton;
    @FXML
    private Label requestLabel;


    private static CreateUserController instance;
    private byte[] photoBytes = null;

    public CreateUserController() {
        instance = this;
    }

    public static void clearFieldsStatic() {
        if (instance != null) {
            instance.clearFields();
        }
    }

    private void clearFields() {
        firstName.clear();
        secondName.clear();
        lastName.clear();
        age.clear();
        phoneNumber.clear();
        email.clear();
        telegramId.clear();
        groups.setValue(null);
        photoBytes = null;
        requestLabel.setText("");
    }
    @FXML
    public void initialize() {
        // Запускаем задачу в фоне
        Task<ObservableList<GroupModel>> task = CommandantService.getAllGroupsTask();

        task.setOnSucceeded(e -> {
            ObservableList<GroupModel> groupList = task.getValue();

            // Добавим пункт "Создать новую группу"
            groupList.add(new GroupModel(-1, "Создать новую группу...", -1));
            groups.setItems(groupList);
        });

        groups.setOnAction(event -> {
            GroupModel selected = groups.getValue();
            if (selected != null && selected.getId() == -1) { // -1 — признак "новой группы"
                createNewGroupDialog();
            }
        });

        task.setOnFailed(e -> {
            System.out.println("Ошибка загрузки групп");
            task.getException().printStackTrace();
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        addPhotoButton.setOnAction(e -> selectPhoto());

        saveButton.setOnAction(e -> {
            try {
               saveResident();
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        });
    }


    private void openCreateParentDialog(Integer residentId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/commandant/createParent.fxml"));
            Parent root = loader.load();

            CreateParentController controller = loader.getController();
            controller.setResidentId(residentId);

            Stage stage = new Stage();
            stage.setTitle("Добавление родителя");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNewGroupDialog() {
        // Простое текстовое диалоговое окно
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Новая группа");
        dialog.setHeaderText("Создание новой группы");
        dialog.setContentText("Введите название новой группы:");

        dialog.showAndWait().ifPresent(groupName -> {
            if (!groupName.trim().isEmpty()) {
                // Создаем задачу на сервере для создания группы
                Task<GroupModel> createGroupTask = CommandantService.createGroup(groupName);

                createGroupTask.setOnSucceeded(e -> {
                    GroupModel newGroup = createGroupTask.getValue();
                    if (newGroup != null) {
                        // Добавляем в список
                        groups.getItems().add(groups.getItems().size() - 1, newGroup); // перед "Создать новую группу"
                        groups.setValue(newGroup); // устанавливаем как выбранный
                    }
                });

                createGroupTask.setOnFailed(e -> {
                    System.out.println("Ошибка создания группы на сервере");
                    createGroupTask.getException().printStackTrace();
                });

                // Запускаем задачу на создание группы
                Thread thread = new Thread(createGroupTask);
                thread.setDaemon(true);
                thread.start();
            }
        });
    }



    private void selectPhoto() {
        // Открытие проводника для выбора файла
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        Stage stage = (Stage) addPhotoButton.getScene().getWindow();
        var file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            // Считывание файла в byte[]
            try {
                photoBytes = java.nio.file.Files.readAllBytes(file.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveResident() throws JsonProcessingException {
        // Создание DTO объекта из введенных данных
        ResidentModel dto = new ResidentModel();
        dto.setFirstName(firstName.getText());
        dto.setSecondName(secondName.getText());
        dto.setLastName(lastName.getText());
        dto.setAge(Integer.parseInt(age.getText()));
        dto.setPhoneNumber(phoneNumber.getText());
        dto.setMail(email.getText());
        dto.setTelegramId(telegramId.getText());
        dto.setPhoto(photoBytes);
        dto.setGroupId(groups.getValue() != null ? groups.getValue().getId() : null);

        // Отправляем данные на сервер (используя метод, описанный ранее)
        String json = JsonBuilder.buildResidentJson(dto);
        Integer residentId = CommandantService.sendResidentJson(json).getId();
        if(residentId != null) {
            requestLabel.setStyle("-fx-text-fill: green;");
            requestLabel.setText("Пользователя создан " + dto.getLastName());
            openCreateParentDialog(residentId);
        } else  {
            requestLabel.setStyle("-fx-text-fill: red;");
            requestLabel.setText("Произошла ошибка создания пользователя");
        }
    }
}

