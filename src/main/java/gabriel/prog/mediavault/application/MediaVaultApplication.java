package gabriel.prog.mediavault.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MediaVaultApplication extends Application {


    /**
     * Starts the application
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MediaVaultApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 400);
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());

        stage.getIcons().add(new Image("gabriel/prog/mediavault/images/vault.png"));
        stage.setTitle("MediaVault");
        stage.setScene(scene);
        stage.show();
    }
}
