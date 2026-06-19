package gabriel.prog.mediavault.dao;

import gabriel.prog.mediavault.media.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for managing the specific SQL DML
 */
public class MediaDao {


    /**
     *
     * @param media The media object to add
     * @return The ID of the added Media or -1 if adding was unsuccessful
     */
    public int addMedia(Media media){
        String sql = "INSERT INTO media (title, category, description, rating, status) VALUES (?, ?, ?, ?, ?)";

        try(Connection connection = DataBaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            statement.setString(1, media.getTitle());
            statement.setString(2, media.getCategory());
            statement.setString(3, media.getDescription());
            statement.setInt(4, media.getRating());
            statement.setString(5, media.getStatus().name());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0){
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()){
                    return generatedKeys.getInt(1);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Updates an existing media entry
     * @param media The updated media object
     * @return true if update was successful
     */
    public boolean updateMedia(Media media){
        String sql = "UPDATE media SET title = ?, category = ?, description = ?, rating = ?, status = ? WHERE id = ?";

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, media.getTitle());
            statement.setString(2, media.getCategory());
            statement.setString(3, media.getDescription());
            statement.setInt(4, media.getRating());
            statement.setString(5, media.getStatus().name());
            statement.setInt(6, media.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Deletes a media entry by ID
     * @param id The ID of the media to delete
     * @return true if deletion was successful
     */
    public boolean deleteMedia(int id){
        String sql = "DELETE FROM media WHERE id = ?";

        try(Connection connection = DataBaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, id);
            return statement.executeUpdate() > 0;

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Retrieves a media entry by ID
     * @param id The ID to search for
     * @return The found media object or null if not found
     */
    public Media getMediaById(int id){
        String sql = "SELECT * FROM media WHERE id = ?";

        try(Connection connection = DataBaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()){
                return mapResultSetToMedia(resultSet);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all media entries
     * @return A list of all media objects
     */
    public List<Media> getAllMedia(){
        List<Media> mediaList = new ArrayList<>();
        String sql = "SELECT * FROM media ORDER BY title";

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)){

            while (resultSet.next()){
                mediaList.add(mapResultSetToMedia(resultSet));
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return mediaList;
    }

    /**
     * Searches media by title. Only has to be a partial match
     * @param searchTerm The search term
     * @return List of matching media objects
     */
    public List<Media> searchMedia(String searchTerm){
        List<Media> mediaList = new ArrayList<>();
        String sql = "SELECT * FROM media WHERE LOWER(title) LIKE LOWER(?) ORDER BY title";

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, "%" + searchTerm + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                mediaList.add(mapResultSetToMedia(resultSet));
            }

        } catch (SQLException e){
            e.printStackTrace();
        }
        return mediaList;
    }

    /**
     * Maps a ResultSet row to a media object
     */
    private Media mapResultSetToMedia (ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt("id");
        String title = resultSet.getString("title");
        String category = resultSet.getString("category");
        String description = resultSet.getString("description");
        int rating = resultSet.getInt("rating");
        MediaStatus status = MediaStatus.valueOf(resultSet.getString("status"));

        return new Media(id, title, category, description, rating, status);
    }

}