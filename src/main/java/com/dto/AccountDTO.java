package com.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private long id;

    private String name;

    private String mail;

    private String username;

    private String password;

    private String address;

    private String dob;

    private String avatar;

    private String phone;

    private String roles;

    private int status;
    private Double balance;

}