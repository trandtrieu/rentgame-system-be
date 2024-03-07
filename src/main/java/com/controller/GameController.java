package com.controller;

import com.dto.GameDTO;
import com.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


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


    @GetMapping("/search")
    public Page<GameDTO> searchAndFilterGames2(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) List<Long> platformIds,
            @RequestParam(required = false) String sortType,
            Pageable pageable) {
        return gameService.searchAndFilterGames2(keyword, categoryIds, platformIds, sortType, pageable);
    }


    @GetMapping("/keywords")
    public ResponseEntity<List<String>> getRandomKeywords() {
        List<String> allGameNames = gameService.getAllGameNames();

        List<String> keywords = getRandomKeywordsFromNames(allGameNames);

        return new ResponseEntity<>(keywords, HttpStatus.OK);
    }

    private List<String> getRandomKeywordsFromNames(List<String> gameNames) {
        Set<String> uniqueKeywords = new HashSet<>();
        Random random = new Random();
        int numOfKeywords = random.nextInt(3) + 5;

        for (String name : gameNames) {
            uniqueKeywords.add(name);
        }


        List<String> result = new ArrayList<>();
        while (result.size() < numOfKeywords && !uniqueKeywords.isEmpty()) {
            int randomIndex = random.nextInt(uniqueKeywords.size());
            Iterator<String> iterator = uniqueKeywords.iterator();
            String keyword = null;
            for (int i = 0; i <= randomIndex; i++) {
                keyword = iterator.next();
            }
            result.add(keyword);
            uniqueKeywords.remove(keyword);
        }

        return result;
    }
}
