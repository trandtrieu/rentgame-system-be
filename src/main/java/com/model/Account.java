package com.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", columnDefinition = "nvarchar(max)")
    private String name;

    @Column
    private String username;

    @Column
    private String mail;

    @Column
    private String password;

    @Column
    private String dob;

    @Column
    private String avatar;

    @Column
    private String address;
    @Column
    private String phone;

    @Column
    private String roles;

    @Column
    private long account_balance;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Wishlist wishList;
}

