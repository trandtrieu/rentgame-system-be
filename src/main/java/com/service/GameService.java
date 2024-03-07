package com.service;

import com.dto.GameDTO;
import com.model.*;
import com.repository.GameImageRepository;
import com.repository.GameRepository;
import com.repository.GameVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameImageRepository gameImageRepository;

    @Autowired
    private GameVideoRepository gameVideoRepository;

    public List<GameDTO> getAllGamesHome() {
        List<Game> games = gameRepository.findAll();
        return games.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Page<GameDTO> getAllGames(Pageable pageable) {
        Page<Game> gamesPage = gameRepository.findAll(pageable);
        return gamesPage.map(this::convertToDTO);
    }

    public Optional<GameDTO> getGameById(long gameId) {
        Optional<Game> game = gameRepository.findById(gameId);
        return game.map(this::convertToDTO);
    }


    public Page<GameDTO> searchAndFilterGames2(String keyword, List<Long> categoryIds, List<Long> platformIds, String sortType, Pageable pageable) {
        if (sortType == null || sortType.isEmpty()) {
            sortType = "";
        }
        Page<Game> games = gameRepository.searchAndFilter(keyword, categoryIds, platformIds, sortType, pageable);
        return games.map(this::convertToDTO);
    }

    public Page<Game> searchAndFilterGames(String keyword, List<Long> categoryIds, List<Long> platformIds, String sortType, Pageable pageable) {
        Page<Game> games;
        if ("desc".equals(sortType)) {
            games = gameRepository.searchAndFilter(keyword, categoryIds, platformIds, sortType, pageable);
        } else if ("asc".equals(sortType)) {
            games = gameRepository.searchAndFilter(keyword, categoryIds, platformIds, sortType, pageable);
        } else {
            games = gameRepository.searchAndFilter(keyword, categoryIds, platformIds, sortType, pageable);
        }
        return games;
    }

    private GameDTO convertToDTO(Game game) {

        GameDTO gameDTO = new GameDTO();
        gameDTO.setId(game.getId());
        gameDTO.setName(game.getName());
        gameDTO.setDescribe(game.getDescribe());
        gameDTO.setDateReleased(game.getDateReleased());
        gameDTO.setAgeLimit(game.getAgeLimit());
        gameDTO.setRating(game.getRating());

        gameDTO.setNote(game.getNote());
        gameDTO.setPrice(game.getPrice());
        gameDTO.setStock(game.getStock());
        List<Game_image> gameImages = gameImageRepository.findByGame(game);
        List<String> imageUrls = gameImages.stream()
                .map(Game_image::getImageUrl)
                .toList();
        gameDTO.setImageUrls(imageUrls);
        List<Game_video> gameVideos = gameVideoRepository.findByGame(game);
        List<String> videoUrls = gameVideos.stream()
                .map(Game_video::getVideoUrl)
                .toList();
        gameDTO.setVideoUrls(videoUrls);

        List<String> categories = game.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toList());
        gameDTO.setCategories(categories);
        List<String> platforms = game.getPlatforms().stream()
                .map(Platform::getName)
                .collect(Collectors.toList());
        gameDTO.setPlatforms(platforms);
        return gameDTO;
    }

    public List<String> getAllGameNames() {
        List<Game> games = gameRepository.findAll();
        return games.stream().map(Game::getName).collect(Collectors.toList());
    }
}
