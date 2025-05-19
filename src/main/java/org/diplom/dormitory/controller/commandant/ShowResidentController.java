package org.diplom.dormitory.controller.commandant;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.diplom.dormitory.model.ResidentDTO;
import org.diplom.dormitory.service.ResidentApiService.CommandantService;

import java.io.ByteArrayInputStream;

public class ShowResidentController {
    @FXML
    private ImageView photoImage;

    @FXML
    private Label fullNameLabel;

    @FXML
    private Label ageLabel;

    @FXML
    private Label phoneNumberLabel;

    @FXML
    private Label mailLabel;

    @FXML
    private ImageView qrCodeImage;

    private Integer residentId;

    public ShowResidentController(Integer residentId) {
        this.residentId = residentId;
    }

    @FXML
    private void initialize() {
        loadResident(residentId);
    }

    private void loadResident(Integer id) {
        Task<ResidentDTO> task = CommandantService.getResidentById(id);

        task.setOnSucceeded(workerStateEvent -> {
            ResidentDTO resident = task.getValue();

            if (resident == null) {
                fullNameLabel.setText("Не удалось загрузить данные");
                return;
            }

            fullNameLabel.setText(resident.getSecondName() + " " + resident.getFirstName() + " " + resident.getLastName());
            ageLabel.setText("Дата рождения: " + resident.getAge());
            phoneNumberLabel.setText("Телефон: " + resident.getPhoneNumber());
            mailLabel.setText("Почта: " + resident.getMail());

            if (resident.getPhoto() != null) {
                Image photo = new Image(new ByteArrayInputStream(resident.getPhoto()));
                photoImage.setImage(photo);
                photoImage.setFitWidth(200);
                photoImage.setFitHeight(150);
                photoImage.setPreserveRatio(true);
                photoImage.setSmooth(true);
            }

            if (resident.getQrCode() != null) {
                Image qr = new Image(new ByteArrayInputStream(resident.getQrCode()));
                qrCodeImage.setImage(qr);
                qrCodeImage.setFitWidth(200);
                qrCodeImage.setFitHeight(150);
                qrCodeImage.setPreserveRatio(true);
                qrCodeImage.setSmooth(true);
            }
        });

        task.setOnFailed(workerStateEvent -> {
            Throwable ex = task.getException();
            ex.printStackTrace();
            fullNameLabel.setText("Ошибка при загрузке данных");
        });

        // Запуск задачи в отдельном потоке
        Thread thread = new Thread(task);
        thread.setDaemon(true); // чтобы не блокировал завершение приложения
        thread.start();
    }

}
