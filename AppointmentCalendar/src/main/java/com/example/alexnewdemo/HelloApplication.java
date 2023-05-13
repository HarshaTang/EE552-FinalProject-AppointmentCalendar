package com.example.alexnewdemo;

import com.teamdev.jxbrowser.browser.Browser;
import com.teamdev.jxbrowser.engine.Engine;
import com.teamdev.jxbrowser.engine.EngineOptions;
import com.teamdev.jxbrowser.view.javafx.BrowserView;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.Instant;

import static com.teamdev.jxbrowser.engine.RenderingMode.HARDWARE_ACCELERATED;

public class HelloApplication extends Application
{
    @Override
    public void start ( Stage stage ) throws IOException
    {
        this.dumpEnv();

        // Initialize Chromium.
        EngineOptions options =
                EngineOptions
                        .newBuilder( HARDWARE_ACCELERATED )
                        .licenseKey( "1BNDHFSC1G68KL633SS3JOPOD2I4DDBK17QY6XH495UKQ3H3XH5GBURXS23VNCFI8WCSTK" )
                        .build();
        Engine engine = Engine.newInstance( options );

        // Create a Browser instance.
        Browser browser = engine.newBrowser();

        // Load the required web page.
        browser.navigation().loadUrl( "https://google.com" );

        // Create and embed JavaFX BrowserView component to display web content.
        BrowserView view = BrowserView.newInstance( browser );

        Scene scene = new Scene( new BorderPane( view ) , 1280 , 800 );
        stage.setTitle( "JxBrowser JavaFX" );
        stage.setScene( scene );
        stage.show();

        // Shutdown Chromium and release allocated resources.
        stage.setOnCloseRequest( event -> engine.close() );
    }

    private void dumpEnv ( )
    {
        System.out.println( "Runtime.version() = " + Runtime.version() );
    }

    private Parent makeParent ( )
    {
        Button btn = new Button();
        btn.setText( "Say 'Hello World'" );
        btn.setOnAction( ( ActionEvent event ) -> System.out.println( "Hello World! " + Instant.now() ) );

        StackPane root = new StackPane();
        root.getChildren().add( btn );
        return root;
    }

    public static void main ( String[] args )
    {
        launch();
    }
}
