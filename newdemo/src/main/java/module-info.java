module com.example.alexnewdemo {
    requires javafx.controls;
    //requires javafx.fxml;
    requires jxbrowser;
    requires jxbrowser.javafx;


    opens com.example.alexnewdemo to javafx.fxml;
    exports com.example.alexnewdemo;
}