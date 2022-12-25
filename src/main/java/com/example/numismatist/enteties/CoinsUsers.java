package com.example.numismatist.enteties;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CoinsUsers {

    @EmbeddedId
    CoinsUsersKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("coinId")
    @JoinColumn(name = "coin_id")
    Coin coin;

    private int count;
}

