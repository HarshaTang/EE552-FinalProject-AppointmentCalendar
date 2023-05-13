module ee552.finalproject.mainapp {
    requires javafx.controls;
    //requires javafx.fxml;
    requires jxbrowser;
    requires jxbrowser.javafx;

    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.lang3;
    requires json.simple;

    //opens ee552.finalproject.mainapp to javafx.fxml;
    exports ee552.finalproject.mainapp;

    // Extra Configurations to save for later ... if needed
    //--add-opens javafx.swing/javafx.embed.swing=jxbrowser
    //--add-opens javafx.graphics/com.sun.javafx.stage=jxbrowser
    // --add-exports javafx.graphics/com.sun.javafx.stage=jxbrowser
    // --add-exports javafx.controls/com.sun.javafx.scene.control=jxbrowser"
}