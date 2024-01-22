package com.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reply")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reply_id;

    @Column(columnDefinition = "nvarchar(max)")
    private String reply_feedback;

    @Column(name = "created_at_time")
    private LocalTime created_at_time;

    @Column(name = "created_date")
    private LocalDate created_date;

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Account account;
}
