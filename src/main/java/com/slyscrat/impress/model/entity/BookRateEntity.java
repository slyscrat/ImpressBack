package com.slyscrat.impress.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "book_rates")
//@IdClass(BookRate.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SequenceGenerator(name="BOOK_RATE_SEQ", sequenceName="bookRate_sequence")
public class BookRateEntity extends AbstractDataBaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_RATE_SEQ")
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    //@Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            nullable = false)
    private UserEntity user;

    //@Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookId",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            nullable = false)
    private BookEntity book;

    @Column(name = "note")
    private String note;

    @Column(name = "rate") //columnDefinition = "SMALLINT"
    private Short rate;
}
