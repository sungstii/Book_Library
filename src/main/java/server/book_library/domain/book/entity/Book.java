package server.book_library.domain.book.entity;

import lombok.Getter;
import lombok.Setter;
import server.book_library.util.BaseEntity;

import javax.persistence.Entity;

@Entity
@Getter @Setter
public class Book extends BaseEntity {
    private String name;
    private String writer;
    private String img_url;
    private String publisher;

}
