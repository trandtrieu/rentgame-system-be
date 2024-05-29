package com.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "rental")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "nick_id")
    private Nick nick;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(name = "rental_start")
    private LocalDateTime rentalStart;

    @Column(name = "rental_end")
    private LocalDateTime rentalEnd;

    @Column
    private String status;

    @Column
    private double totalAmount;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;


    // Constructors, getters, setters
}
