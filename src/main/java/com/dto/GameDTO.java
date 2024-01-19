package com.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {


    private long id;
    private String name;
    private String date_released;
    private String describe;
    private String price;
    private String age_limit;
    private String platform;
    private String stock;
    private String note;
    private int rating;
    private String imageUrls;
    private String videoUrls;

}