package server.book_library.library.library.entity;

import lombok.Getter;
import lombok.Setter;
import server.book_library.util.BaseEntity;

import javax.persistence.Entity;

@Entity
@Getter @Setter
public class Library extends BaseEntity {
    private String name;
    private String img_url;
    private String location;

}
