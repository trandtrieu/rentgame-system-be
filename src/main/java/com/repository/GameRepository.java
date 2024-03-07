package com.repository;

import com.model.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends PagingAndSortingRepository<Game, Long> {

    Optional<Game> findById(long gameId);


    List<Game> findAll();


    @Query("SELECT g FROM Game g " +
            "LEFT JOIN g.categories c " +
            "LEFT JOIN g.platforms p " +
            "WHERE " +
            "(COALESCE(:keyword, '') = '' OR " +
            "g.name LIKE %:keyword% OR " +
            "g.describe LIKE %:keyword%) AND " +

            "(:categoryIds IS NULL OR c.id IN :categoryIds) AND " +
            "(:platformIds IS NULL OR p.id IN :platformIds) " +
            "ORDER BY CASE WHEN :sortType = 'desc' THEN g.price END DESC, " +
            "CASE WHEN :sortType = 'asc' THEN g.price END ASC")
    Page<Game> searchAndFilter(String keyword, List<Long> categoryIds, List<Long> platformIds, String sortType, Pageable pageable);

    Page<Game> findAllByOrderByPriceDesc(Pageable pageable);

    Page<Game> findAllByOrderByPriceAsc(Pageable pageable);
}
