package com.controller;

import com.dto.GameDTO;
import com.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/rent-game/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @GetMapping("/home")
    public List<GameDTO> getAllGames() {
        return gameService.getAllGamesHome();
    }

    // Inside GameController class

    @GetMapping
    public ResponseEntity<Page<GameDTO>> getAllGames(
            @RequestParam(defaultValue = "newest") String sortType,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String dateRelease,
            Pageable pageable) {
        try {
            Page<GameDTO> gamesPage = gameService.getFilteredGames(sortType, searchTerm, categoryId, dateRelease, pageable);

            long totalElements = gamesPage.getTotalElements();
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(totalElements));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(gamesPage);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> getGameById(@PathVariable("id") long gameId) {
        try {
            Optional<GameDTO> game = gameService.getGameById(gameId);
            return game.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/newest")
    public ResponseEntity<Page<GameDTO>> getGamesByNewestRelease(Pageable pageable) {
        try {
            Page<GameDTO> gamesPage = gameService.getGamesByNewestRelease(pageable);
            return new ResponseEntity<>(gamesPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/oldest")
    public ResponseEntity<Page<GameDTO>> getGamesByOldestRelease(Pageable pageable) {
        try {
            Page<GameDTO> gamesPage = gameService.getGamesByOldestRelease(pageable);
            return new ResponseEntity<>(gamesPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Inside GameController class

    @GetMapping("/{categoryId}")
    public ResponseEntity<Page<GameDTO>> getGamesByCategory(@PathVariable long categoryId, Pageable pageable) {
        try {
            Page<GameDTO> gamesPage = gameService.getGamesByCategory(categoryId, pageable);
            return new ResponseEntity<>(gamesPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
