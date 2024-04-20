package com.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RentalDTO {

    private long id;
    private long gameId;
    private String gameName;
    private LocalDate rentalDate;
    private LocalDate returnDate;

    private String status;


}