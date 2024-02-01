package com.repository;

import com.model.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends PagingAndSortingRepository<Game, Long> {

    Optional<Game> findById(long gameId);

    Page<Game> findAllByOrderByDateReleasedDesc(Pageable pageable);

    Page<Game> findAllByOrderByDateReleasedAsc(Pageable pageable);

    List<Game> findAll();

    Page<Game> findByCategories_Id(long categoryId, Pageable pageable);

    // Trong GameRepository interface
    Page<Game> findByCategories_IdAndDateReleased(Long categoryId, String dateRelease, Pageable pageable);

    // Trong GameRepository interface
    Page<Game> findByDateReleased(String dateRelease, Pageable pageable);

    Page<Game> findByNameContainingIgnoreCaseOrDescribeContainingIgnoreCase(String name, String describe, Pageable pageable);

    Page<Game> findByNameContainingIgnoreCaseOrCategories_NameContainingIgnoreCaseOrDateReleasedContainingIgnoreCase(String name, String category, String dateRelease, Pageable pageable);

}
