package uz.pdp.demo9.config.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class User {
    private Integer id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Integer photoId;

    public User(HttpServletRequest request){
        this.email = request.getParameter("email");
        this.password = request.getParameter("password");
        this.firstName = request.getParameter("firstName");
        this.lastName = request.getParameter("lastName");
    }

    @SneakyThrows
    public User(ResultSet resultSet){
        this.email = resultSet.getString("email");
        this.password = resultSet.getString("password");
        this.firstName = resultSet.getString("first_name");
        this.lastName = resultSet.getString("last_name");
        this.photoId = resultSet.getInt("photo_id");
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}

