package com.example.numismatist.enteties;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class CoinsUsersKey implements Serializable {


    @Column(name = "user_id")
    Integer userId;

    @Column(name = "coin_id")
    Integer coinId;

}
