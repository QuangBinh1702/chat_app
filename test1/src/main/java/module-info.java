module src.test1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens src.test1 to javafx.fxml;
    exports src.test1;
}