package com.controller;

import com.dto.RentalDTO;
import com.exception.GameNotFoundException;
import com.exception.OutOfStockException;
import com.exception.RentalNotFoundException;
import com.exception.UserNotFoundException;
import com.service.*;
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
    public ResponseEntity<?> rentGame(@RequestParam long userId, @RequestParam long gameId, @RequestParam int hours) {
        try {
            RentalDTO rentalDTO = rentalService.rentAccountByHour(userId, gameId, hours);
            return new ResponseEntity<>(rentalDTO, HttpStatus.OK);
        } catch (UserNotFoundException | GameNotFoundException | OutOfStockException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to rent game: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnGame(@RequestParam long rentalId) {
        try {
            rentalService.returnAccount(rentalId);
            return new ResponseEntity<>("Game returned successfully", HttpStatus.OK);
        } catch (RentalNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to return game: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RentalDTO>> getRentedGamesByUser(@PathVariable long userId) {
        List<RentalDTO> rentedGames = rentalService.getRentedAccountsByUser(userId);
        if (rentedGames.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(rentedGames, HttpStatus.OK);
    }
}