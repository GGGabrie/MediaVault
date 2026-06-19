module gabriel.prog.mediavault {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens gabriel.prog.mediavault to javafx.fxml;
    exports gabriel.prog.mediavault;
    exports gabriel.prog.mediavault.controller;
    opens gabriel.prog.mediavault.controller to javafx.fxml;
    exports gabriel.prog.mediavault.application;
    opens gabriel.prog.mediavault.application to javafx.fxml;
}