package com.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NickDTO {
    private long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String note;
    private String status;
    private Long rentedBy;
    private LocalDateTime rentalDate;
    private LocalDateTime returnDate;
    private long remainingTime;
    private List<GameDTO> games; // List of games associated with the account

}