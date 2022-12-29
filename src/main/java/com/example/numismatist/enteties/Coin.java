package com.example.numismatist.enteties;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.Set;

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
    @JoinColumn(name = "id_nominal")
    private Nominal nominal;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_material")
    private Material material;
    private String weight;
    private String diameter;
    private String length;
    private String width;
    private String thickness;
    private String circulation;
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
    private String pureMetal;
    private String quality;
    private boolean isInvestment;

    @OneToMany(mappedBy = "coin")
    Set<CoinsUsers> counts;

    public Coin() {
    }
}
