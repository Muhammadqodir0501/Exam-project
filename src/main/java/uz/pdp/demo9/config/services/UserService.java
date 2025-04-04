package uz.pdp.demo9.config.services;

import lombok.SneakyThrows;
import uz.pdp.demo9.config.DbConfig;
import uz.pdp.demo9.config.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.sql.SQLException;

public class UserService {
    public static User save(User user) throws SQLException {
        try (Connection connection = DbConfig.getDataSource().getConnection()) {
            connection.setAutoCommit(true); // Ensure changes are committed
            String sql = "INSERT INTO users (email, password, first_name, last_name, photo_id) VALUES (?,?,?,?,?) RETURNING id";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setInt(5, user.getPhotoId());
            System.out.println("Executing SQL: " + sql + " with values: " + user);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                int id = rs.getInt("id");
                user.setId(id);
                System.out.println("User saved with ID: " + id);
                return user;
            } else {
                throw new SQLException("No ID returned from insert");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in UserService.save: " + e.getMessage());
            throw e;
        }
    }

    public static Optional<User> findByEmail(String email) throws SQLException {
        try (Connection connection = DbConfig.getDataSource().getConnection()) {
            connection.setAutoCommit(true);
            String sql = "SELECT * FROM users WHERE email = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, email);
            System.out.println("Executing SQL: " + sql + " with email: " + email);
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPhotoId(rs.getInt("photo_id"));
                System.out.println("User found: " + user);
                return Optional.of(user);
            } else {
                System.out.println("No user found with email: " + email);
                return Optional.empty();
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in UserService.findByEmail: " + e.getMessage());
            throw e;
        }
    }
}
