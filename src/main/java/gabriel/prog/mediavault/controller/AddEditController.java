package gabriel.prog.mediavault.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import gabriel.prog.mediavault.media.*;
import gabriel.prog.mediavault.service.MediaService;

import java.io.IOException;
import java.util.List;


/**
 * Controller for adding or editing a media entry
 */
public class AddEditController {

    @FXML TextField titleField;
    @FXML ComboBox<String> categoryCombo;
    @FXML ComboBox<String> ratingCombo;
    @FXML ComboBox<String> statusCombo;
    @FXML TextArea descriptionArea;

    Media editingMedia;
    MediaService mediaService;


    /**
     * Shows a dialog for adding or editing media
     * @param media The media to edit (null for new media)
     * @param mediaService The service to use
     */
    public static void showDialog(Media media, MediaService mediaService){
        try{
            FXMLLoader loader = new FXMLLoader(
                    AddEditController.class.getResource("AddEdit-view.fxml")
            );
            DialogPane dialogPane = loader.load();
            AddEditController controller = loader.getController();
            controller.setMediaService(mediaService);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);

            if (media == null) {
                dialog.setTitle("Neues Medium hinzufügen");
                controller.setEditingMedia(null);
            } else{
                dialog.setTitle("Medium bearbeiten: " + media.getTitle());
                controller.setEditingMedia(media);
                controller.loadMediaData(media);
            }

            controller.loadCategories();

            dialog.showAndWait().ifPresent(result -> {
                if (result == ButtonType.OK){
                    controller.saveMedia();
                }
            });
        } catch (IOException e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Dialog konnte nicht geladen werden");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Initializes relevant FXML data
     */
    @FXML
    public void initialize() {
        ratingCombo.getItems().addAll("1", "2", "3", "4", "5");

        statusCombo.getItems().addAll("Gesehen", "Nicht gesehen");
        statusCombo.setValue("Nicht gesehen");

        categoryCombo.setEditable(true);
    }

    /**
     * Loads all current categories
     */
    private void loadCategories() {
        if (mediaService != null){
            List<String> categories = mediaService.getAllCategories();
            categoryCombo.getItems().clear();
            categoryCombo.getItems().addAll(categories);
            if (categories.isEmpty()){
                categoryCombo.getItems().add("Film");
                categoryCombo.getItems().add("Serie");
                categoryCombo.getItems().add("Buch");
            }
        }
    }

    /**
     * Reads media data from given media object into fx components
     * @param media The media to be read from
     */
    private void loadMediaData(Media media){
        titleField.setText(media.getTitle());
        categoryCombo.setValue(media.getCategory());
        ratingCombo.setValue(String.valueOf(media.getRating()));
        statusCombo.setValue(media.getStatus().getDisplayName());
        descriptionArea.setText(media.getDescription());
    }

    /**
     * Writes changes to media or new media into databank
     */
    private void saveMedia(){
        try{
            String title = titleField.getText().trim();
            String category = categoryCombo.getValue();
            Integer rating = Integer.parseInt(ratingCombo.getValue());
            String statusStr = statusCombo.getValue();
            String description = descriptionArea.getText();

            if (title.isEmpty()){
                showError("Bitte geben Sie einen Titel ein.");
                return;
            }
            if (category == null || category.isEmpty()){
                showError("Bitte geben Sie eine Kategorie ein.");
                return;
            }
            if (rating < 1 || rating > 5){
                showError("Bitte wählen Sie eine Bewertung.");
                return;
            }
            if (statusStr == null) {
                showError("Bitte wählen Sie einen Status.");
                return;
            }

            MediaStatus status = statusStr.equals("Gesehen") ? MediaStatus.SEEN : MediaStatus.UNSEEN;

            boolean success;
            if (editingMedia == null){
                Media newMedia = mediaService.addMedia(title, category, description, rating, status);
                success = newMedia!= null;
            }
            else {
                success = mediaService.updateMedia(editingMedia.getId(), title, category, description, rating, status);
            }

            if (!success){
                showError("Fehler beim Speichern des Mediums.");
            }
        } catch (IllegalArgumentException e){
            showError(e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            showError("Ein unerwarteter Fehler ist aufgetreten: " + e.getMessage());
        }
    }

    // Utility Method
    private void showError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Setters for mediaService and editingMedia
    private void setMediaService(MediaService mediaService){
        this.mediaService = mediaService;
    }

    private void setEditingMedia(Media media){
        this.editingMedia = media;
    }
}