module com.example.kobi {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.sql;


    opens com.example.kobi to javafx.fxml;
    exports com.example.kobi;
}