package uz.pdp.demo9.config.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Publication {
    private Integer id;
    private Integer userId;
    private String title;
    private String description;
    private Integer publicationPhotoId;
    private String adminName; // New field for the admin's name
    private List<Comment> comments;

    public Publication(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.userId = rs.getInt("user_id");
        this.title = rs.getString("title");
        this.description = rs.getString("description");
        this.publicationPhotoId = rs.getInt("publication_photo_id");
    }
}