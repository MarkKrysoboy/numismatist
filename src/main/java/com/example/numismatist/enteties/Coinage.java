package com.example.numismatist.enteties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Coinage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_coinage;

    private String coinage;

    public Coinage() {
    }
}
