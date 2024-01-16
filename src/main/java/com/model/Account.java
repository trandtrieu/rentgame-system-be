package com.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
	

}

