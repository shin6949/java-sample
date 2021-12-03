package me.cocoblue.hibertest.dto;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Builder
@Table(name="book",
        uniqueConstraints={@UniqueConstraint(columnNames={"isbn"})})
public class Book {
    @Id
    @Column(name = "isbn", nullable = false)
    private long isbn;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "author", nullable = false)
    private String author;
    @Column(name = "status", nullable = false)
    private Boolean status;
    @Column(name = "location", nullable = false)
    private String location;
    @JoinColumn(name = "borrowed_user")
    @OneToOne
    private User borrowedUser;
}
