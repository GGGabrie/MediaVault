package gabriel.prog.mediavault.service;

import gabriel.prog.mediavault.dao.MediaDao;
import gabriel.prog.mediavault.media.Media;
import gabriel.prog.mediavault.media.MediaStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MediaService {
    final MediaDao mediaDAO;

    public MediaService() {
        this.mediaDAO = new MediaDao();
    }


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


    public boolean deleteMedia(int id){
        return mediaDAO.deleteMedia(id);
    }

    public Media getMediaById(int id){
        return mediaDAO.getMediaById(id);
    }

    public List<Media> getAllMedia() {
        return mediaDAO.getAllMedia();
    }

    public List<Media> getMediaByCategory(String category){
        return mediaDAO.getMediaByCategory(category);
    }

    public List<Media> getMediaByRating(int rating){
        return mediaDAO.getMediaByRating(rating);
    }

    public List<Media> getMediaByStatus(MediaStatus status){
        return mediaDAO.getMediaByStatus(status);
    }

    public List<Media> searchMedia(String searchTerm){
        if (searchTerm == null || searchTerm.trim().isEmpty()){
            return getAllMedia();
        }
        return mediaDAO.searchMedia(searchTerm);
    }

    public List<String> getAllCategories(){
        return mediaDAO.getAllMedia().stream()
                .map(Media::getCategory)
                .distinct()
                .sorted()
                .toList();
    }

    public Map<String, Long> getCategoryStatistics() {
        return mediaDAO.getAllMedia().stream()
                .collect(Collectors.groupingBy(Media::getCategory, Collectors.counting()));
    }

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