module com.example.recipesjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.recipesjavafx to javafx.fxml;
    exports com.example.recipesjavafx;
}