package com.cocoblue.libraryapp.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
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
    @Column(name = "borrow_count", nullable = false)
    private int borrowCount;
}
