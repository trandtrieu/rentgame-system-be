package com.service;

import com.dto.GameDTO;
import com.model.Category;
import com.model.Game;
import com.model.Game_image;
import com.model.Game_video;
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

    private GameDTO convertToDTO(Game game) {

        GameDTO gameDTO = new GameDTO();
        gameDTO.setId(game.getId());
        gameDTO.setName(game.getName());
        gameDTO.setDescribe(game.getDescribe());
        gameDTO.setDateReleased(game.getDateReleased());
        gameDTO.setAge_limit(game.getAge_limit());
        gameDTO.setNote(game.getNote());
        gameDTO.setPlatform(game.getPlatform());
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
        return gameDTO;
    }

    public Page<GameDTO> getGamesByNewestRelease(Pageable pageable) {
        Page<Game> games = gameRepository.findAllByOrderByDateReleasedDesc(pageable);
        return games.map(this::convertToDTO);
    }

    public Page<GameDTO> getGamesByOldestRelease(Pageable pageable) {
        Page<Game> games = gameRepository.findAllByOrderByDateReleasedAsc(pageable);
        return games.map(this::convertToDTO);
    }
    // Inside GameService class

    public Page<GameDTO> getGamesByCategory(long categoryId, Pageable pageable) {
        Page<Game> games = gameRepository.findByCategories_Id(categoryId, pageable);
        return games.map(this::convertToDTO);
    }

    // Trong GameService class
    public Page<GameDTO> getGamesByCategoryAndDateRelease(Long categoryId, String dateRelease, Pageable pageable) {
        // Lọc theo cả category và dateRelease
        Page<Game> games = gameRepository.findByCategories_IdAndDateReleased(categoryId, dateRelease, pageable);
        return games.map(this::convertToDTO);
    }

    // Trong GameService class
    public Page<GameDTO> getGamesByDateRelease(String dateRelease, Pageable pageable) {
        // Lọc theo dateRelease
        Page<Game> games = gameRepository.findByDateReleased(dateRelease, pageable);
        return games.map(this::convertToDTO);
    }

    public Page<GameDTO> getGamesBySearchTerm(String searchTerm, Pageable pageable) {
        // Lọc theo từ khóa tìm kiếm trong tên hoặc mô tả của game
        Page<Game> games = gameRepository.findByNameContainingIgnoreCaseOrDescribeContainingIgnoreCase(searchTerm, searchTerm, pageable);
        return games.map(this::convertToDTO);
    }

    public Page<GameDTO> getFilteredGames(String sortType, String searchTerm, Long categoryId, String dateRelease, Pageable pageable) {
        Page<Game> games;

        if (categoryId != null && dateRelease != null) {
            games = gameRepository.findByCategories_IdAndDateReleased(categoryId, dateRelease, pageable);
        } else if (categoryId != null) {
            games = gameRepository.findByCategories_Id(categoryId, pageable);
        } else if (dateRelease != null) {
            games = gameRepository.findByDateReleased(dateRelease, pageable);
        } else if (searchTerm != null) {
            games = gameRepository.findByNameContainingIgnoreCaseOrCategories_NameContainingIgnoreCaseOrDateReleasedContainingIgnoreCase(searchTerm, searchTerm, searchTerm, pageable);
        } else {
            if ("newest".equals(sortType)) {
                games = gameRepository.findAllByOrderByDateReleasedDesc(pageable);
            } else if ("oldest".equals(sortType)) {
                games = gameRepository.findAllByOrderByDateReleasedAsc(pageable);
            } else {
                games = gameRepository.findAll(pageable);
            }
        }

        return games.map(this::convertToDTO);
    }

}
