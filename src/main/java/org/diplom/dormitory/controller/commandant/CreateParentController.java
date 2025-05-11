package org.diplom.dormitory.controller.commandant;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Data;
import org.diplom.dormitory.model.GroupModel;
import org.diplom.dormitory.model.ParentModel;
import org.diplom.dormitory.model.ResidentModel;
import org.diplom.dormitory.model.ResidentParentModel;
import org.diplom.dormitory.service.ResidentApiService.CommandantService;
import org.diplom.dormitory.util.JsonBuilder;

import java.util.Objects;

@Data
public class CreateParentController {

    @FXML private TextField firstName;
    @FXML private TextField secondName;
    @FXML private TextField lastName;
    @FXML private TextField phoneNumber;
    @FXML private TextField email;
    @FXML private TextField telegramId;
    @FXML private Button saveButton;

    private Integer residentId;

    @FXML
    public void initialize() {
        saveButton.setOnAction(e -> {
            try {
                saveParent();
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void saveParent() throws JsonProcessingException {
        ParentModel parent = new ParentModel();
        parent.setFirstName(firstName.getText());
        parent.setSecondName(secondName.getText());
        parent.setLastName(lastName.getText());
        parent.setPhoneNumber(phoneNumber.getText());
        parent.setMail(email.getText());
        parent.setTelegramId(telegramId.getText());

        String json = JsonBuilder.buildParentJson(parent);
        Integer parentId = Objects.requireNonNull(CommandantService.createParent(json)).getId();

        if (parentId != null) {
            ResidentParentModel dto = new ResidentParentModel();
            dto.setResidentId(residentId);
            dto.setParentId(parentId);
            CommandantService.addParentAndResident(JsonBuilder.buildResidentParentJson(dto));

            // Показ диалога
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Добавить еще одного родителя?");
            alert.setHeaderText("Родитель добавлен успешно!");
            alert.setContentText("Хотите ли вы добавить еще одного родителя?");

            ButtonType yesButton = new ButtonType("Да");
            ButtonType noButton = new ButtonType("Нет");
            alert.getButtonTypes().setAll(yesButton, noButton);

            alert.showAndWait().ifPresent(response -> {
                if (response == yesButton) {
                    clearFields(); // Очищаем поля и остаемся на этом же окне
                } else {
                    Stage stage = (Stage) saveButton.getScene().getWindow();
                    stage.close();

                    // Очищаем CreateUserController (передадим сигнал)
                    CreateUserController.clearFieldsStatic(); // Статический метод для очистки
                }
            });
        }
    }

    // Метод очистки полей
    private void clearFields() {
        firstName.clear();
        secondName.clear();
        lastName.clear();
        phoneNumber.clear();
        email.clear();
        telegramId.clear();
    }

}

