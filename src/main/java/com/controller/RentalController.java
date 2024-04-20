package com.controller;

import com.dto.RentalDTO;
import com.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/rent-game/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @PostMapping("/rent")
    public ResponseEntity<String> rentAccount(@RequestParam long userId, @RequestParam long gameId) {
        try {
            rentalService.rentAccount(userId, gameId);
            return new ResponseEntity<>("Account rented successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to rent account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnAccount(@RequestParam long rentalId) {
        try {
            rentalService.returnAccount(rentalId);
            return new ResponseEntity<>("Account returned successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to return account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RentalDTO>> getRentedAccountsByUser(@PathVariable long userId) {
        List<RentalDTO> rentedAccounts = rentalService.getRentedAccountsByUser(userId);
        if (rentedAccounts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(rentedAccounts, HttpStatus.OK);
    }

    // Other methods such as getting all rentals, getting rental details by ID, etc. can be added here

}