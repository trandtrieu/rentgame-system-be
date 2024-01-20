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

    public List<GameDTO> getAllGames() {
        List<Game> games = gameRepository.findAll();
        return games.stream().map(this::convertToDTO).collect(Collectors.toList());
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
        gameDTO.setDate_released(game.getDate_released());
        gameDTO.setAge_limit(game.getAge_limit());
        gameDTO.setNote(game.getNote());
        gameDTO.setPlatform(game.getNote());
        gameDTO.setPrice(game.getPrice());
        gameDTO.setStock(game.getStock());
        List<Game_image> gameImages = gameImageRepository.findByGame(game);
        List<String> imageUrls = gameImages.stream()
                .map(Game_image::getImageUrl)
                .toList();
        gameDTO.setImageUrls(imageUrls.toString());
        List<Game_video> gameVideos = gameVideoRepository.findByGame(game);
        List<String> videoUrls = gameVideos.stream()
                .map(Game_video::getVideoUrl)
                .toList();
        gameDTO.setVideoUrls(videoUrls.toString());

        List<String> categories = game.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toList());
        gameDTO.setCategories(categories);
        return gameDTO;
    }
}
