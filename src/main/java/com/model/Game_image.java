package com.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "game_image")
public class Game_image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int image_id;

    @Column
    private String imageUrl;

    @Column
    private int type;
    // style 6x9: type 0, landscape: type 1

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;
}
