module com.jade.dreamjournalv2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.jade.dreamjournalv2 to javafx.fxml;
    exports com.jade.dreamjournalv2;
}