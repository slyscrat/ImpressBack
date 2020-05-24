package com.slyscrat.impress.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookEntity extends AbstractDataBaseEntity {
    @Id
    @NotNull
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "publicationDate", nullable = false, length = 4)
    private String publicationDate;

    @NotNull
    @Column(name = "icon", nullable = false)
    private String icon;

    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tag_to_book",
            joinColumns = {@JoinColumn(name = "bookId", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tagId", referencedColumnName = "id")},
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT)
    )
    private Set<BookTagEntity> tags = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "book",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<BookRateEntity> rated = new HashSet<>();
}
