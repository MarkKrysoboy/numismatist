package com.example.numismatist.enteties;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "series")
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_series;

    private String seriesName;

    @Column(columnDefinition="TEXT")
    private String linkToBankPage;

    public Series() {
    }
}
