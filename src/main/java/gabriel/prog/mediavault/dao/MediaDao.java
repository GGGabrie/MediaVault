package gabriel.prog.mediavault.dao;

import gabriel.prog.mediavault.media.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MediaDao {


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

    public List<Media> getMediaByCategory(String category){
        List<Media> mediaList = new ArrayList<>();
        String sql = "SELECT * FROM media WHERE LOWER(category) = LOWER(?) ORDER BY title";

        try(Connection connection = DataBaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1,category);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                mediaList.add(mapResultSetToMedia(resultSet));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return mediaList;
    }

    public List<Media> getMediaByRating(int rating){
        List<Media> mediaList = new ArrayList<>();
        String sql = "SELECT * FROM media WHERE rating >= ? ORDER BY rating DESC";

        try (Connection connection = DataBaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1,rating);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                mediaList.add(mapResultSetToMedia(resultSet));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return mediaList;
    }

    public List<Media> getMediaByStatus(MediaStatus status){
        List<Media> mediaList = new ArrayList<>();
        String sql = "SELECT * FROM media WHERE status = ? ORDER BY title";

        try(Connection connection = DataBaseConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, status.name());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                mediaList.add(mapResultSetToMedia(resultSet));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return mediaList;
    }

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