package com.controller;

import com.dto.GameDTO;
import com.model.Category;
import com.model.Game;
import com.model.Game_image;
import com.model.Game_video;
import com.repository.CategoryRepository;
import com.repository.GameImageRepository;
import com.repository.GameRepository;
import com.repository.GameVideoRepository;
import com.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/rent-game/games")
public class GameController {
    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameImageRepository gameImageRepository;

    @Autowired
    private GameVideoRepository gameVideoRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<GameDTO> getAllGames() {
        return gameService.getAllGames();
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




}
