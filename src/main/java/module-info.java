module com.example.recipesjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.example.recipesjavafx to javafx.fxml;
    exports com.example.recipesjavafx;
}