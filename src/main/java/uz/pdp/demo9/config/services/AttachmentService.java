package uz.pdp.demo9.config.services;

import lombok.SneakyThrows;
import uz.pdp.demo9.config.DbConfig;
import uz.pdp.demo9.config.entity.Attachment;

import javax.servlet.http.Part;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AttachmentService {
    @SneakyThrows
    public static Attachment save(Attachment attachment) {
        try (
                Connection connection = DbConfig.getDataSource().getConnection();
        ) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO attachment (content) VALUES (?) returning id");
            preparedStatement.setBytes(1, attachment.getContent());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            resultSet.next();
            int id = resultSet.getInt("id");
            attachment.setId(id);
            return attachment;
        }


    }
}
