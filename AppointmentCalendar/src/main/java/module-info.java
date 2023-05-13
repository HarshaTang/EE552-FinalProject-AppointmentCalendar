module com.example.alexnewdemo {
    requires javafx.controls;
    //requires javafx.fxml;
    requires jxbrowser;
    requires jxbrowser.javafx;

    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.lang3;
    requires json.simple;

    opens com.example.alexnewdemo to javafx.fxml;
    exports com.example.alexnewdemo;
}