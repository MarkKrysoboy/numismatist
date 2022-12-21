package com.example.numismatist.enteties;

import jakarta.persistence.*;
import jdk.jfr.DataAmount;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Coin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_series")
    private Series series;
    private Date releaseDate;
    private String catalogNumber;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_niminal")
    private Nominal nominal;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_material")
    private Material material;
    private Double weight;
    private Double diameter;
    private Double thickness;
    private Integer circulation;
    @Column(columnDefinition="TEXT")
    private String obverse;
    @Column(columnDefinition="TEXT")
    private String reverse;
    @Column(columnDefinition="TEXT")
    private String authors;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_coinage")
    private Coinage coinage;
    @Column(columnDefinition="TEXT")
    private String designHerd;
    @Column(columnDefinition="TEXT")
    private String historicalReference;
    @Column(columnDefinition="TEXT")
    private String linkToBankPage;
    @Lob
    private String obverseImage;
    @Lob
    private String reverseImage;

    public Coin() {
    }
}
