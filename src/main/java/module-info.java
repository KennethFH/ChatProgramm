module at.fhtw.chatprogramm {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.fhtw.chatprogramm to javafx.fxml;
    exports at.fhtw.chatprogramm;
}