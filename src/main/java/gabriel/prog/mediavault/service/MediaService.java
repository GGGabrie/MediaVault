package gabriel.prog.mediavault.service;

import gabriel.prog.mediavault.dao.MediaDao;
import gabriel.prog.mediavault.media.Media;
import gabriel.prog.mediavault.media.MediaStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Layer for handling media operations
 */
public class MediaService {
    final MediaDao mediaDAO;

    /**
     * Constructor
     */
    public MediaService() {
        this.mediaDAO = new MediaDao();
    }


    /**
     * Adds a new media entry
     * @param title Media title
     * @param category Media category
     * @param description Media description
     * @param rating Media rating
     * @param status Media status
     * @return The newly created media object with ID, or null if failed
     */
    public Media addMedia(String title, String category, String description, int rating, MediaStatus status){

        if (title == null || title.trim().isEmpty()){
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (category == null || category.trim().isEmpty()){
            throw new IllegalArgumentException("Category cannot be empty");
        }
        if (rating < 1 || rating > 5){
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Media media = new Media(title.trim(), category.trim(), description, rating, status);
        int id = mediaDAO.addMedia(media);

        if (id > 0){
            media.setId(id);
            return media;
        }
        return null;
    }

    /**
     * Updates an existing media entry
     * @param title Media title
     * @param category Media category
     * @param description Media description
     * @param rating Media rating
     * @param status Media status
     * @return true if successful
     */
    public boolean updateMedia(int id, String title, String category, String description, int rating, MediaStatus status){
        if (title == null || title.trim().isEmpty()){
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (rating < 1 || rating > 5){
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Media media = new Media(id, title.trim(), category. trim(), description, rating, status);
        return mediaDAO.updateMedia(media);
    }


    /**
     * Deletes media entry based on ID
     * @param id Media ID
     * @return true if successful
     */
    public boolean deleteMedia(int id){
        return mediaDAO.deleteMedia(id);
    }

    /**
     * Gets a media entry by ID
     * @param id Media ID
     * @return Found media, if unsuccessful null
     */
    public Media getMediaById(int id){
        return mediaDAO.getMediaById(id);
    }

    /**
     * Gets all media entries
     * @return A list with all media objects
     */
    public List<Media> getAllMedia() {
        return mediaDAO.getAllMedia();
    }

    /**
     * Searches media by title. Only has to be a partial match
     * @param searchTerm The search term
     * @return List of matching media objects
     */
    public List<Media> searchMedia(String searchTerm){
        if (searchTerm == null || searchTerm.trim().isEmpty()){
            return getAllMedia();
        }
        return mediaDAO.searchMedia(searchTerm);
    }

    /**
     * Gets all categories currently present
     * @return A list of all currently present categories
     */
    public List<String> getAllCategories(){
        return mediaDAO.getAllMedia().stream()
                .map(Media::getCategory)
                .distinct()
                .sorted()
                .toList();
    }

    /**
     * Toggles the status between seen and unseen
     * @param id The id by which the media is addressed
     * @return true if successful, false if not
     */
    public boolean toggleStatus(int id){
        Media media = getMediaById(id);
        if (media == null){
            return false;
        }

        MediaStatus newStatus = media.getStatus() == MediaStatus.SEEN ? MediaStatus.UNSEEN: MediaStatus.SEEN;

        media.setStatus(newStatus);
        return mediaDAO.updateMedia(media);
    }
}