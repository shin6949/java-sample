package com.cocoblue.libraryapp.dto;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
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
    @Column(name = "borrow_count", nullable = false)
    private int borrowCount;

    public String getStatusToString() {
        return status ? "대출 가능" : "대출 불가";
    }
}
