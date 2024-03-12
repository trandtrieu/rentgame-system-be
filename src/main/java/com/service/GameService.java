package com.service;

import com.controller.FeedbackController;
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


    public Page<GameDTO> searchAndFilterGames(String keyword, List<Long> categoryIds, List<Long> platformIds, String sortType, Pageable pageable) {
        if (sortType == null || sortType.isEmpty()) {
            sortType = "";
        }
        Page<Game> games = gameRepository.searchAndFilter(keyword, categoryIds, platformIds, sortType, pageable);
        return games.map(this::convertToDTO);
    }

    public Page<GameDTO> searchAndFilterGames2(String keyword, List<Long> categoryIds, List<Long> platformIds, String sortType, Double minPrice, Double maxPrice, Pageable pageable) {
        if (sortType == null || sortType.isEmpty()) {
            sortType = "";
        }
        Page<Game> games = gameRepository.searchAndFilter2(keyword, categoryIds, platformIds, sortType, minPrice, maxPrice, pageable);
        return games.map(this::convertToDTO);
    }


    private GameDTO convertToDTO(Game game) {

        GameDTO gameDTO = new GameDTO();
        FeedbackController feedbackController = new FeedbackController();
        gameDTO.setId(game.getId());
        gameDTO.setName(game.getName());
        gameDTO.setDescribe(game.getDescribe());
        gameDTO.setDateReleased(game.getDateReleased());
        gameDTO.setAgeLimit(game.getAgeLimit());
        gameDTO.setNote(game.getNote());
        double averageRating = calculateAverageRatingForGame(game);
        gameDTO.setRating(averageRating);
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

    public double calculateAverageRatingForGame(Game game) {
        List<Feedback> feedbacks = game.getFeedbackList();
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }

        int totalRating = 0;
        for (Feedback feedback : feedbacks) {
            totalRating += feedback.getRating();
        }

        double averageRating = (double) totalRating / feedbacks.size();

        // Làm tròn số về 1 chữ số thập phân
        double roundedAverageRating = Math.round(averageRating * 10.0) / 10.0;

        return roundedAverageRating;
    }


    public Game getProductById(Long gameId) {
        return gameRepository.findById(gameId).orElse(null);
    }
}
