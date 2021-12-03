package com.cocoblue.libraryapp.dto;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="user",
        uniqueConstraints={@UniqueConstraint(columnNames={"id"})})
public class User {
    @Id
    private long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "status", nullable = false)
    private Boolean status;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;
    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin;
}
