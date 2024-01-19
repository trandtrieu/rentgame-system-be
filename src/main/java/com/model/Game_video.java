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
@Table(name = "game_video")
public class Game_video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int video_id;

    @Column
    private String videoUrl;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;
}