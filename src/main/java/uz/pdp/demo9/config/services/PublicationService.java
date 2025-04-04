package uz.pdp.demo9.config.services;

import uz.pdp.demo9.config.DbConfig;
import uz.pdp.demo9.config.entity.Attachment;
import uz.pdp.demo9.config.entity.Publication;
import uz.pdp.demo9.config.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PublicationService {

    public static Publication save(Publication publication, Integer userId) throws SQLException {
        try (Connection connection = DbConfig.getDataSource().getConnection()) {
            connection.setAutoCommit(true); // Ensure changes are committed
            String sql = "INSERT INTO publications (user_id, title, description, publication_photo_id) VALUES (?,?,?,?) RETURNING id";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, userId);
            ps.setString(2, publication.getTitle());
            ps.setString(3, publication.getDescription());
            ps.setInt(4, publication.getPublicationPhotoId());

            System.out.println("Executing SQL: " + sql + " with values: " + publication);

            ps.execute();

            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                int id = rs.getInt("id");
                publication.setId(id);
                System.out.println("Publication saved with ID: " + id);
                return publication;
            } else {
                throw new SQLException("No ID returned from insert");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in PublicationService.save: " + e.getMessage());
            throw e;
        }
    }

    public static Optional<Publication> findById(Integer publicationId) throws SQLException {
        try (Connection connection = DbConfig.getDataSource().getConnection()) {
            connection.setAutoCommit(true);
            String sql = "SELECT * FROM publications WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, publicationId);
            System.out.println("Executing SQL: " + sql + " with id: " + publicationId);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                Publication publication = new Publication(rs);
                System.out.println("Publication found: " + publication);
                return Optional.of(publication);
            } else {
                System.out.println("No publication found with id: " + publicationId);
                return Optional.empty();
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in PublicationService.findById: " + e.getMessage());
            throw e;
        }
    }

    public static List<Publication> findAll() throws SQLException {
        List<Publication> publications = new ArrayList<>();
        try (Connection connection = DbConfig.getDataSource().getConnection()) {
            connection.setAutoCommit(true);
            String sql = "SELECT * FROM publications ORDER BY id DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            System.out.println("Executing SQL: " + sql);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Publication publication = new Publication(rs);
                publications.add(publication);
            }
            System.out.println("Found " + publications.size() + " publications");
        } catch (SQLException e) {
            System.err.println("SQL Error in PublicationService.findAll: " + e.getMessage());
            throw e;
        }
        return publications;
    }
}
