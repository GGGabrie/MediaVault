package gabriel.prog.mediavault.media;


/**
 * Represents a media entity in the collection
 */
public class Media {
    int id;
    String title;
    String category;
    String description;
    int rating;
    MediaStatus status;


    /**
     * Constructor for new Media without ID
     * @param title Media title
     * @param category Media category
     * @param description Media description
     * @param rating Media rating
     * @param status Media status
     */
    public Media(String title, String category, String description, int rating, MediaStatus status){
        this.title = title;
        this.category = category;
        this.description = description;
        this.rating = rating;
        this.status = status;
    }

    /**
     * Constructor for existing media in database
     * @param title Media title
     * @param category Media category
     * @param description Media description
     * @param rating Media rating
     * @param status Media status
     */
    public Media(int id, String title, String category, String description, int rating, MediaStatus status){
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.rating = rating;
        this.status = status;
    }


    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }

    public MediaStatus getStatus() {
        return status;
    }
    public void setStatus(MediaStatus status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return String.format("%s (%s) - %d★", title, category, rating);
    }
}
