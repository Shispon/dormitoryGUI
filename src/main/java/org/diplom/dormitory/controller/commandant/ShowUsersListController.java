package org.diplom.dormitory.controller.commandant;

import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.diplom.dormitory.MainApp;
import org.diplom.dormitory.model.ResidentDTO;
import org.diplom.dormitory.model.RoleModel;
import org.diplom.dormitory.model.StaffDTO;
import org.diplom.dormitory.service.ResidentApiService.CommandantService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowUsersListController {

    @FXML
    private Button returnBackButton;

    @FXML
    private ListView<String> usersListView;

    @FXML
    private MenuItem chooseResidentsMenuItem;

    @FXML
    private MenuItem chooseStaffMenuItem;

    @FXML
    private TextField enterTextField;

    private List<RoleModel> allRoles = new ArrayList<>();
    private List<ResidentDTO> allResidents = new ArrayList<>();
    private List<StaffDTO> allStaff = new ArrayList<>();

    private enum ViewMode {
        RESIDENTS,
        STAFF
    }

    private ViewMode currentMode = ViewMode.RESIDENTS;

    @FXML
    private void initialize() {
        returnBackButton.setOnAction(event -> {
            try {
                MainApp.showCommandantWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        loadRoles(); // Загружаем роли для отображения должностей

        chooseResidentsMenuItem.setOnAction(e -> loadResidents());
        chooseStaffMenuItem.setOnAction(e -> loadStaff());

        // Слушатель текста для поиска
        enterTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (currentMode == ViewMode.RESIDENTS) {
                filterResidents(newText);
            } else {
                filterStaff(newText);
            }
        });

        // Обработчик клика по элементу списка
        usersListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // двойной клик
                int selectedIndex = usersListView.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0) {
                    if (currentMode == ViewMode.RESIDENTS) {
                        ResidentDTO selectedResident = allResidents.get(selectedIndex);
                        showResidentWindow(selectedResident.getId());
                    }
                    // (Аналогично позже можно сделать и для STAFF)
                }
            }
        });
    }


    private void showResidentWindow(Integer residentId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/commandant/showResident.fxml"));

            // передача контроллера с параметром
            loader.setControllerFactory(param -> new ShowResidentController(residentId));

            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Информация о жильце");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // окно блокирует предыдущее
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private RoleModel getRoleName(Integer roleId) {
        return allRoles.stream()
                .filter(role -> role.getId().equals(roleId))
                .findFirst()
                .orElse(null);
    }

    private void loadRoles() {
        Task<ObservableList<RoleModel>> task = CommandantService.getAllRolesTask();

        task.setOnSucceeded(e -> {
            allRoles = new ArrayList<>(task.getValue());
        });

        new Thread(task).start();
    }

    @FXML
    private void loadResidents() {
        currentMode = ViewMode.RESIDENTS;

        Task<List<ResidentDTO>> task = CommandantService.getAllResidentsTask();

        task.setOnSucceeded(event -> {
            allResidents = task.getValue();
            updateListView(allResidents);
        });

        new Thread(task).start();
    }

    @FXML
    private void loadStaff() {
        currentMode = ViewMode.STAFF;

        Task<List<StaffDTO>> task = CommandantService.getAllStaffTask();

        task.setOnSucceeded(event -> {
            allStaff = task.getValue();
            updateListView(allStaff);
        });

        new Thread(task).start();
    }

    private void updateListView(List<?> items) {
        ObservableList<String> displayList = FXCollections.observableArrayList();

        for (Object item : items) {
            if (item instanceof ResidentDTO resident) {
                displayList.add(
                        resident.getLastName() + " " +
                                resident.getFirstName() + " " +
                                resident.getSecondName()
                );
            } else if (item instanceof StaffDTO staff) {
                RoleModel role = getRoleName(staff.getRoleId());
                String roleName = (role != null) ? role.getRoleName() : "Неизвестная должность";

                displayList.add(
                        staff.getLastName() + " " +
                                staff.getFirstName() + " " +
                                staff.getSecondName() +
                                " — " + roleName
                );
            }
        }

        usersListView.setItems(displayList);
    }

    private void filterResidents(String query) {
        ObservableList<String> filtered = FXCollections.observableArrayList();

        for (ResidentDTO resident : allResidents) {
            String fullName = resident.getLastName() + " " + resident.getFirstName() + " " + resident.getSecondName();
            if (fullName.toLowerCase().contains(query.toLowerCase())) {
                filtered.add(fullName);
            }
        }

        usersListView.setItems(filtered);
    }

    private void filterStaff(String query) {
        ObservableList<String> filtered = FXCollections.observableArrayList();

        for (StaffDTO staff : allStaff) {
            RoleModel role = getRoleName(staff.getRoleId());
            String roleName = (role != null) ? role.getRoleName() : "Неизвестная должность";

            String fullInfo = staff.getLastName() + " " +
                    staff.getFirstName() + " " +
                    staff.getSecondName() +
                    " — " + roleName;

            if (fullInfo.toLowerCase().contains(query.toLowerCase())) {
                filtered.add(fullInfo);
            }
        }

        usersListView.setItems(filtered);
    }
}
