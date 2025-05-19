module org.dormitory.dormitory {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core; // Для работы с Jackson
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires okhttp3;
    requires java.desktop; // Для работы с OkHttp
    requires java.net.http;
    requires static lombok;
    requires kafka.clients;

    opens org.diplom.dormitory.model to com.fasterxml.jackson.databind;
    opens org.diplom.dormitory.controller to javafx.fxml; // Открываем пакет контроллеров для FXMLLoader
    exports org.diplom.dormitory;
    opens org.diplom.dormitory.controller.commandant to javafx.fxml; // Экспортируем основной пакет приложения
    exports org.diplom.dormitory.controller.commandant;
    opens org.diplom.dormitory.controller.security to javafx.fxml;
}