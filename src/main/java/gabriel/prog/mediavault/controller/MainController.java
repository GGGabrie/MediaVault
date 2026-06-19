package gabriel.prog.mediavault.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import gabriel.prog.mediavault.media.*;
import gabriel.prog.mediavault.service.MediaService;

import java.util.List;
import java.util.Optional;

public class MainController {
    private final MediaService mediaService;
    private ObservableList<Media> mediaData;

    @FXML private TableView<Media> mediaTable;
    @FXML private ComboBox<String> categoryFilter;
    @FXML private ComboBox<String> ratingFilter;
    @FXML private ComboBox<String> statusFilter;
    @FXML private TextField searchField;
    @FXML private Label statusLabel;

    public MainController(){
        this.mediaService = new MediaService();
        this.mediaData = FXCollections.observableArrayList();
    }

    public void initialize(){
        loadMediaData();
        setupTableColumns();
        setupFilters();
        refreshMediaList();
        statusLabel.setText("Bereit - " + mediaData.size() + " Medien gefunden");
    }

    private void loadMediaData(){
        try {
            List<Media> allMedia = mediaService.getAllMedia();
            mediaData.setAll(allMedia);
            mediaTable.setItems(mediaData);
        } catch (Exception e){
            e.printStackTrace();
            showAlert("Datenbankfehler", "Konnte keine Medien aus der Datenbank laden: " + e.getMessage());
        }
    }

    private void setupTableColumns(){
        TableColumn<Media, String> titleCol = (TableColumn<Media, String>) mediaTable.getColumns().get(0);
        TableColumn<Media,String> categoryCol = (TableColumn<Media, String>) mediaTable.getColumns().get(1);
        TableColumn<Media,String> ratingCol = (TableColumn<Media, String>) mediaTable.getColumns().get(2);
        TableColumn<Media,String> statusCol = (TableColumn<Media, String>) mediaTable.getColumns().get(3);
        TableColumn<Media,String> descCol = (TableColumn<Media, String>) mediaTable.getColumns().get(4);

        titleCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
        categoryCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategory()));
        ratingCol.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getRating()).asObject().asString());
        statusCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus().getDisplayName()));
        descCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));

        mediaTable.setItems(mediaData);
    }

    private void setupFilters(){
        categoryFilter.getItems().add("Alle");
        refreshCategories();
        categoryFilter.setValue("Alle");

        ratingFilter.getItems().addAll("Alle", "1★", "2★", "3★", "4★", "5★");
        ratingFilter.setValue("Alle");

        statusFilter.getItems().addAll("Alle", "Gesehen", "Nicht gesehen");
        statusFilter.setValue("Alle");
    }

    private void refreshCategories(){
        List<String> categories = mediaService.getAllCategories();
        categoryFilter.getItems().clear();
        categoryFilter.getItems().add("Alle");
        categoryFilter.getItems().addAll(categories);
        if (!categoryFilter.getItems().contains(categoryFilter.getValue())){
            categoryFilter.setValue("Alle");
        }
    }

    private void refreshMediaList(){
        String category = categoryFilter.getValue();
        String ratingStr = ratingFilter.getValue();
        String statusStr = statusFilter.getValue();
        String search = searchField.getText();

        List<Media> filteredList;

        if (search != null && !search.trim().isEmpty()){
            filteredList = mediaService.searchMedia(search);
        } else {
            filteredList = mediaService.getAllMedia();
        }

        if (category != null && !"Alle".equals(category)){
            filteredList = filteredList.stream()
                    .filter(m -> m.getCategory().equalsIgnoreCase(category))
                    .toList();
        }

        if (ratingStr != null && !"Alle".equals(ratingStr)){
            int rating = Integer.parseInt(ratingStr.replace("★", ""));
            filteredList = filteredList.stream()
                    .filter(m -> m.getRating() == rating)
                    .toList();
        }

        if (statusStr != null && !"Alle".equals(statusStr)){
            MediaStatus status = statusStr.equals("Gesehen") ? MediaStatus.SEEN : MediaStatus.UNSEEN;

            filteredList = filteredList.stream()
                    .filter(m -> m.getStatus() == status)
                    .toList();
        }

        mediaData.setAll(filteredList);
        statusLabel.setText(String.format("%d Medien gefunden", mediaData.size()));
    }

    @FXML
    private void handleRefresh(){
        refreshCategories();
        refreshMediaList();
        statusLabel.setText("Aktualisiert. " + mediaData.size() + " Medien gefunden");
    }

    @FXML
    private void handleFilterChange(){
        refreshMediaList();
    }

    @FXML
    private void handleSearch(){
        refreshMediaList();
    }

    @FXML
    private void handleClearSearch(){
        searchField.clear();
        refreshMediaList();
    }

    @FXML
    private void handleAddMedia(){
        AddEditController.showDialog(null, mediaService);
        handleRefresh();
    }

    @FXML
    private void handleEditMedia(){
        Media selected = mediaTable.getSelectionModel().getSelectedItem();
        if (selected == null){
            showAlert("Keine Auswahl", "Bitte wählen Sie ein Medium zum Bearbeiten aus.");
            return;
        }
        AddEditController.showDialog(selected, mediaService);
        handleRefresh();
    }

    @FXML
    private void handleDeleteMedia(){
        Media selected = mediaTable.getSelectionModel().getSelectedItem();
        if (selected == null){
            showAlert("Keine Auswahl", "Bitte wählen Sie ein Medium zum Löschen aus.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Löschen bestätigen");
        alert.setHeaderText("Möchten Sie dieses Medium wirklich löschen?");
        alert.setContentText(selected.getTitle() + " (" + selected.getCategory() + ")");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            boolean deleted = mediaService.deleteMedia(selected.getId());
            if (deleted){
                handleRefresh();
                statusLabel.setText("Medium gelöscht: " + selected.getTitle());
            } else {
                showAlert("Fehler", "Das Medium konnte nicht gelöscht werden.");
            }
        }
    }

    @FXML
    private void handleToggleStatus(){
        Media selected = mediaTable.getSelectionModel().getSelectedItem();
        if (selected == null){
            showAlert("Keine Auswahl", "Bitte wählen Sie ein Medium aus.");
            return;
        }
        mediaService.toggleStatus(selected.getId());
        handleRefresh();
    }

    @FXML
    private void handleShowDetails(){
        Media selected = mediaTable.getSelectionModel().getSelectedItem();
        if (selected == null){
            showAlert("Keine Auswahl", "Bitte wählen Sie ein Medium aus.");
            return;
        }
        DetailController.showDialog(selected);
    }


    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}