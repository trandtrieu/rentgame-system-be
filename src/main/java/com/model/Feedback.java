package com.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long feedback_id;



    @Column(name = "rating")
    private int rating;

    @Column(name = "comment", columnDefinition = "nvarchar(max)")
    private String comment;

    @Column(name = "created_at_time")
    private LocalTime created_at_time;

    @Column(name = "created_date")
    private LocalDate created_date;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Account user;


    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL)
    private List<Reply> replyList;



}
