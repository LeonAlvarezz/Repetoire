module com.example.repertoire {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.repertoire to javafx.fxml;
    exports com.example.repertoire;
}