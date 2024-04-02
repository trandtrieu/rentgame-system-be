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
    private Double price;
    private String ageLimit;
    private String stock;
    private String note;
    private double rating;
    private List<String> imageUrls;
    private List<Integer> imageTypes;
    private List<String> videoUrls;
    private List<String> categories;
    private List<String> platforms;


}