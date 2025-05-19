package org.diplom.dormitory.controller.security;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import org.diplom.dormitory.model.ResidentDTO;
import org.diplom.dormitory.util.KafkaConsumerThread;

import java.io.ByteArrayInputStream;

public class CheckResidentController {
    @FXML
    private ImageView photoImage;
    @FXML private Label fullNameLabel;
    @FXML private Label ageLabel;
    @FXML private Label phoneNumberLabel;
    @FXML private Label mailLabel;
    @FXML private Label presentLabel;

    private KafkaConsumerThread consumerThread;

    @FXML
    public void initialize() {
        // Запускаем consumer при открытии окна
        consumerThread = new KafkaConsumerThread(this::updateUI);
        consumerThread.start();
    }

    // Вызывается из KafkaConsumerThread
    private void updateUI(ResidentDTO dto) {
        if (dto == null) return;

        Platform.runLater(() -> {
            fullNameLabel.setText(dto.getSecondName() + " " + dto.getFirstName() + " " + dto.getLastName());
            ageLabel.setText("Дата рождения: " + dto.getAge());
            phoneNumberLabel.setText("Телефон: " + dto.getPhoneNumber());
            mailLabel.setText("Почта: " + dto.getMail());

            // Фото
            if (dto.getPhoto() != null) {
                Image photo = new Image(new ByteArrayInputStream(dto.getPhoto()));
                photoImage.setImage(photo);
                photoImage.setFitWidth(200);
                photoImage.setFitHeight(150);
                photoImage.setPreserveRatio(true);
                photoImage.setSmooth(true);
            }

            // Присутствие
            if (Boolean.TRUE.equals(dto.getIsPresent())) {
                presentLabel.setText("Вошёл");
                presentLabel.setTextFill(Color.GREEN);
            } else {
                presentLabel.setText("Вышел");
                presentLabel.setTextFill(Color.RED);
            }
        });
    }

    // Метод, чтобы остановить consumer при закрытии окна
    public void stopKafka() {
        if (consumerThread != null) {
            consumerThread.stopConsuming();
        }
    }
}
