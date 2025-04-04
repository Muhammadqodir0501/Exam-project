package uz.pdp.demo9.config.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Comment {
    private Integer id;
    private Integer userId;
    private Integer publicationId;
    private String content;
    private Date createdAt;
    private String userName;
}