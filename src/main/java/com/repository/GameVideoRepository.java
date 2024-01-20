package com.repository;

import com.model.Game;
import com.model.Game_video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameVideoRepository extends JpaRepository<Game_video, Long> {

    List<Game_video> findByGame(Game game);

}
