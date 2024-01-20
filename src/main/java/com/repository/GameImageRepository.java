package com.repository;

import com.model.Game;
import com.model.Game_image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameImageRepository extends JpaRepository<Game_image, Long> {
    List<Game_image> findByGame(Game game);

}
