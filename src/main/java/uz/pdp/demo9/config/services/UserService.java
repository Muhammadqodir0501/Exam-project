package uz.pdp.demo9.config.services;

import lombok.SneakyThrows;
import uz.pdp.demo9.config.DbConfig;
import uz.pdp.demo9.config.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class UserService {
    @SneakyThrows
    public static User save (User user) {
        try(
                Connection connection = DbConfig.getDataSource().getConnection();
        ){
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (email, password, first_name, last_name, photo_id) VALUES (?,?,?,?,?) returning id");
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setInt(5, user.getPhotoId());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            resultSet.next();
            int id = resultSet.getInt("id");
            user.setId(id);
            return user;
        }

    }

    @SneakyThrows
    public static Optional<User> findByEmail(String email) {
        try (
                Connection connection = DbConfig.getDataSource().getConnection();
                ){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
            preparedStatement.setString(1, email);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                User user = new User(resultSet);
                return Optional.of(user);
            }else{
                return Optional.empty();
            }
        }
    }
}
