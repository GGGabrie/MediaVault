package gabriel.prog.mediavault.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MediaVaultApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MediaVaultApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 400);

        stage.getIcons().add(new Image("gabriel/prog/mediavault/images/vault.png"));
        stage.setTitle("MediaVault");
        stage.setScene(scene);
        stage.show();
    }
}
