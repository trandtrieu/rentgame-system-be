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
public class ReplyDTO {
    private long reply_id;
    private String reply_feedback;
    private String created_at_time;
    private LocalDate created_date;
    private int feedback_id;
    private long user_id;
    private String user_name;
    private String avatar;
    private String roles;


}
