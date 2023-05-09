package frontend;

/*
import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.view.javafx.BrowserView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
*/

public class DisplayCalendar /* extends Application  */ {
	
	/*
    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();
    }
	*/
	/*
     @Override
     public void start(Stage primaryStage) {
         // Initialize Chromium.
         Engine engine = Engine.newInstance(HARDWARE_ACCELERATED);

         Browser browser = engine.newBrowser();

         // Load the required web page.
         browser.navigation().loadUrl("https://html5test.com");

         // Create and embed JavaFX BrowserView component to display web content.
         BrowserView view = BrowserView.newInstance(browser);

         Scene scene = new Scene(new StackPane(view), 1280, 800);
         primaryStage.setTitle("JxBrowser JavaFX");
         primaryStage.setScene(scene);
         primaryStage.show();

         // Shutdown Chromium and release allocated resources.
         primaryStage.setOnCloseRequest(event -> engine.close());
     }

    public static void main(String[] args) {
        launch();
    }
	*/
}
