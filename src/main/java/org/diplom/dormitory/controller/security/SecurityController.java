package org.diplom.dormitory.controller.security;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import org.diplom.dormitory.MainApp;

import java.io.IOException;

public class SecurityController {
    @FXML
    private MenuItem propetryMenuItem;
    @FXML private MenuItem exitMenuItem;
    @FXML private MenuItem checkResidentMenuItem;
    @FXML private MenuItem hereListMenuItem;
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
        checkResidentMenuItem.setOnAction(event -> {
            loadForm("/fxml/security/checkResident.fxml");
        });
//        hereListMenuItem.setOnAction(event -> {
//            loadForm("/fxml/commandant/chooseList.fxml");
//        });
    }
    private void loadForm(String fxmlPath) {
        try {
            // 🛑 Остановка Kafka, если внутри VBox был CheckResidentController
            if (!dynamicContentPane.getChildren().isEmpty()) {
                Node oldNode = dynamicContentPane.getChildren().get(0);
                Object userData = oldNode.getUserData();
                if (userData instanceof CheckResidentController controller) {
                    controller.stopKafka();
                }
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node content = loader.load();

            // ✅ Устанавливаем ссылку на контроллер, чтобы потом можно было снова его остановить
            content.setUserData(loader.getController());

            dynamicContentPane.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
