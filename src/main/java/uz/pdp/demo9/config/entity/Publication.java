package uz.pdp.demo9.config.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Publication {
    private Integer id;
    private Integer userId;
    private String title;
    private String description;
    private Integer publicationPhotoId;
}
