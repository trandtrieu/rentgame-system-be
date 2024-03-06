package com.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDTO {
    private long feedback_id;
    private double rating;
    private String comment;
    private String created_at_time;
    private LocalDate created_date;
    private long game_id;
    private String g_name;
    private long user_id;
    private String user_name;
    private String avatar;
    private String roles;
}
