package gabriel.prog.mediavault.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import gabriel.prog.mediavault.media.Media;

import java.io.IOException;


public class DetailController {
    @FXML Label titleLabel;
    @FXML Label categoryLabel;
    @FXML Label ratingLabel;
    @FXML Label statusLabel;
    @FXML TextArea descriptionArea;

    public static void showDialog(Media media){
        try{
            FXMLLoader loader = new FXMLLoader(
                    DetailController.class.getResource("Detail-view.fxml"));
            Scene scene = new Scene(loader.load(), 500,350);

            DetailController controller = loader.getController();
            controller.setMediaData(media);

            Stage stage = new Stage();
            stage.setTitle("Details: " + media.getTitle());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void setMediaData(Media media){
        titleLabel.setText(media.getTitle());
        categoryLabel.setText(media.getCategory());

        String stars = "★".repeat(media.getRating()) + "☆".repeat(5- media.getRating());
        ratingLabel.setText(stars + " (" + media.getRating() + "/5)");

        statusLabel.setText(media.getStatus().getDisplayName());
        descriptionArea.setText(media.getDescription());
    }

    @FXML
    private void handleClose() {
        ((Stage) descriptionArea.getScene().getWindow()).close();
    }
}

