package org.diplom.dormitory.service.ResidentApiService;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.diplom.dormitory.model.*;
import org.diplom.dormitory.util.JsonBuilder;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class CommandantService {

    public static StaffModel createStaff (String json) {
        try {
            URL url = new URL("http://localhost:8080/api/staff");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 201 || responseCode == 200) {
                try (InputStream inputStream = connection.getInputStream()) {
                    return JsonBuilder.getObjectMapper().readValue(inputStream, StaffModel.class);
                }
            } else {
                System.err.println("Ошибка: " + responseCode);
                try (InputStream errorStream = connection.getErrorStream()) {
                    if (errorStream != null) {
                        Scanner scanner = new Scanner(errorStream, StandardCharsets.UTF_8).useDelimiter("\\A");
                        if (scanner.hasNext()) {
                            System.out.println("Ответ с ошибкой: " + scanner.next());
                        }
                    }
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String addParentAndResident(String json) {
        try {
            URL url = new URL("http://localhost:8080/api/resident_parent");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 201 || responseCode == 200) {
                try (Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8)) {
                    return scanner.hasNext() ? scanner.nextLine() : null;
                }
            } else {
                System.err.println("Ошибка: " + responseCode);
                try (InputStream errorStream = connection.getErrorStream()) {
                    if (errorStream != null) {
                        Scanner scanner = new Scanner(errorStream, StandardCharsets.UTF_8).useDelimiter("\\A");
                        if (scanner.hasNext()) {
                            System.out.println("Ответ с ошибкой: " + scanner.next());
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static ParentModel createParent (String json) {
        try {
            URL url = new URL("http://localhost:8080/api/parent");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 201 || responseCode == 200) {
                try (InputStream inputStream = connection.getInputStream()) {
                    return JsonBuilder.getObjectMapper().readValue(inputStream, ParentModel.class);
                }
            } else {
                System.err.println("Ошибка: " + responseCode);
                try (InputStream errorStream = connection.getErrorStream()) {
                    if (errorStream != null) {
                        Scanner scanner = new Scanner(errorStream, StandardCharsets.UTF_8).useDelimiter("\\A");
                        if (scanner.hasNext()) {
                            System.out.println("Ответ с ошибкой: " + scanner.next());
                        }
                    }
                }

            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResidentModel sendResidentJson(String json) {
        try {
            URL url = new URL("http://localhost:8080/api/residents");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // Отправляем JSON
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 201 || responseCode == 200) {
                // Читаем JSON-ответ
                try (InputStream inputStream = connection.getInputStream()) {
                    return JsonBuilder.getObjectMapper().readValue(inputStream, ResidentModel.class);
                }
            } else {
                System.err.println("Ошибка: " + responseCode);
                try (InputStream errorStream = connection.getErrorStream()) {
                    if (errorStream != null) {
                        Scanner scanner = new Scanner(errorStream, StandardCharsets.UTF_8).useDelimiter("\\A");
                        if (scanner.hasNext()) {
                            System.out.println("Ответ с ошибкой: " + scanner.next());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Task<Integer> setCuratorInGroup(Integer groupId, Integer curatorId) {
        return new Task<>() {
            @Override
            protected Integer call() {
                try {
                    // Формируем URL с параметрами
                    String urlString = "http://localhost:8080/api/groups?groupId=" + groupId + "&curatorId=" + curatorId;
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("PUT"); // Устанавливаем метод запроса
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setDoOutput(true); // PUT требует doOutput(true), даже если тело пустое

                    connection.connect();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        return 1; // Успех
                    } else {
                        System.err.println("Ошибка: код ответа " + responseCode);
                        return -1; // Ошибка со стороны сервера/клиента
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return -2; // Прочая ошибка
                }
            }
        };
    }


    public static Task<ObservableList<GroupModel>> getAllGroupsTask() {
        return new Task<>() {
            @Override
            protected ObservableList<GroupModel> call() {
                try {
                    URL url = new URL("http://localhost:8080/api/groups");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "application/json");

                    if (connection.getResponseCode() == 200) {
                        ObjectMapper mapper = new ObjectMapper();
                        List<GroupModel> groups = mapper.readValue(
                                connection.getInputStream(),
                                mapper.getTypeFactory().constructCollectionType(List.class, GroupModel.class)
                        );
                        return FXCollections.observableArrayList(groups);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return FXCollections.observableArrayList();
            }
        };
    }

    public static Task<ObservableList<RoleModel>> getAllRolesTask() {
        return new Task<>() {
            @Override
            protected ObservableList<RoleModel> call() {
                try {
                    URL url = new URL("http://localhost:8080/api/roles");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "application/json");

                    if (connection.getResponseCode() == 200) {
                        ObjectMapper mapper = new ObjectMapper();
                        List<RoleModel> roles = mapper.readValue(
                                connection.getInputStream(),
                                mapper.getTypeFactory().constructCollectionType(List.class, RoleModel.class)
                        );
                        return FXCollections.observableArrayList(roles);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return FXCollections.observableArrayList();
            }
        };
    }


    public static Task<GroupModel> createGroup(String name) {
        return new Task<>() {
            @Override
            protected GroupModel call() throws Exception {
                try {
                    // Пример отправки POST-запроса на создание новой группы
                    var url = new URL("http://localhost:8080/api/groups");
                    var conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type", "application/json");

                    // JSON: {"name": "Группа X"}
                    String json = "{\"groupName\":\"" + name + "\"}";
                    try (OutputStream os = conn.getOutputStream()) {
                        os.write(json.getBytes(StandardCharsets.UTF_8));
                    }

                    if (conn.getResponseCode() == 200 || conn.getResponseCode() == 201) {
                        // Читаем ответ, десериализуем в GroupModel
                        ObjectMapper mapper = new ObjectMapper();
                        return mapper.readValue(conn.getInputStream(), GroupModel.class);
                    } else {
                        System.out.println("Ошибка при создании группы: " + conn.getResponseCode());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null; // Если не получилось создать группу
            }
        };
    }
}
