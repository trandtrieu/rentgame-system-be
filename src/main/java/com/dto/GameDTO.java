package com.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    private long id;
    private String name;
    private String dateReleased;
    private String describe;
    private String price;
    private String age_limit;
    private String platform;
    private String stock;
    private String note;
    private int rating;
    private List<String> imageUrls;
    private List<String> videoUrls;
    private List<String> categories;

}