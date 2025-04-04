package uz.pdp.demo9.config.services;

import uz.pdp.demo9.config.DbConfig;
import uz.pdp.demo9.config.entity.Comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentService {

    public static Comment save(Comment comment) throws SQLException {
        try (Connection connection = DbConfig.getDataSource().getConnection()) {
            connection.setAutoCommit(true);
            String sql = "INSERT INTO comments (user_id, publication_id, content, created_at) VALUES (?,?,?,?) RETURNING id";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, comment.getUserId());
            ps.setInt(2, comment.getPublicationId());
            ps.setString(3, comment.getContent());
            ps.setTimestamp(4, new java.sql.Timestamp(comment.getCreatedAt().getTime()));

            System.out.println("Executing SQL: " + sql + " with values: " + comment);
            ps.execute();

            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                int id = rs.getInt("id");
                comment.setId(id);
                System.out.println("Comment saved with ID: " + id);
                return comment;
            } else {
                throw new SQLException("No ID returned from insert");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in CommentService.save: " + e.getMessage());
            throw e;
        }
    }

    public static List<Comment> findByPublicationId(Integer publicationId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = DbConfig.getDataSource().getConnection()) {
            connection.setAutoCommit(true);
            String sql = "SELECT c.*, u.first_name, u.last_name " +
                    "FROM comments c " +
                    "JOIN users u ON c.user_id = u.id " +
                    "WHERE c.publication_id = ? " +
                    "ORDER BY c.created_at DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, publicationId);
            System.out.println("Executing SQL: " + sql + " with publication_id: " + publicationId);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            while (rs.next()) {
                Comment comment = new Comment();
                comment.setId(rs.getInt("id"));
                comment.setUserId(rs.getInt("user_id"));
                comment.setPublicationId(rs.getInt("publication_id"));
                comment.setContent(rs.getString("content"));
                comment.setCreatedAt(rs.getTimestamp("created_at"));
                comment.setUserName(rs.getString("first_name") + " " + rs.getString("last_name"));
                comments.add(comment);
            }
            System.out.println("Found " + comments.size() + " comments for publication ID: " + publicationId);
        } catch (SQLException e) {
            System.err.println("SQL Error in CommentService.findByPublicationId: " + e.getMessage());
            throw e;
        }
        return comments;
    }
}