package com.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "nick")
public class Nick {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String note;

    @Column
    private String status;

    @Column
    private Long rentedBy;

    @Column(name = "rental_start")
    private LocalDateTime rentalStart;

    @Column(name = "rental_end")
    private LocalDateTime rentalEnd;

    @ManyToMany
    @JoinTable(
            name = "nick_game",
            joinColumns = @JoinColumn(name = "nick_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private Set<Game> games = new HashSet<>();


    @OneToMany(mappedBy = "nick")
    private Set<Rental> rentals = new HashSet<>();

    public Rental getRental() {
        // Trả về rental đầu tiên nếu tồn tại, hoặc null nếu không có rental nào
        return rentals.isEmpty() ? null : rentals.iterator().next();
    }
}
