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


//    @Query("SELECT g FROM Game g " +
//            "LEFT JOIN g.categories c " +
//            "LEFT JOIN g.platforms p " +
//            "WHERE " +
//            "(COALESCE(:keyword, '') = '' OR " +
//            "g.name LIKE %:keyword% OR " +
//            "g.describe LIKE %:keyword%) AND " +
//
//            "(:categoryIds IS NULL OR c.id IN :categoryIds) AND " +
//            "(:platformIds IS NULL OR p.id IN :platformIds) " +
//            "ORDER BY CASE WHEN :sortType = 'desc' THEN g.price END DESC, " +
//            "CASE WHEN :sortType = 'asc' THEN g.price END ASC")
//    Page<Game> searchAndFilter(String keyword, List<Long> categoryIds, List<Long> platformIds, String sortType, Pageable pageable);


    @Query("SELECT g " +
            "FROM Game g " +
            "LEFT JOIN g.categories c " +
            "LEFT JOIN g.platforms p " +
            "LEFT JOIN g.feedbackList f " +  // Join the feedbackList for rating calculation
            "WHERE " +
            "(COALESCE(:keyword, '') = '' OR " +
            "g.name LIKE %:keyword% OR " +
            "g.describe LIKE %:keyword%) AND " +
            "(:categoryIds IS NULL OR c.id IN :categoryIds) AND " +
            "(:platformIds IS NULL OR p.id IN :platformIds) " +
            "GROUP BY g " +  // Group by the game entity
            "ORDER BY CASE WHEN :sortType = 'desc' THEN g.price END DESC, " +
            "CASE WHEN :sortType = 'asc' THEN g.price END ASC, " +
            "CASE WHEN :sortType = 'rating_desc' THEN AVG(f.rating) END DESC, " +  // Sort by rating in descending order
            "CASE WHEN :sortType = 'rating_asc' THEN AVG(f.rating) END ASC")
        // Sort by rating in ascending order
    Page<Game> searchAndFilter(String keyword, List<Long> categoryIds, List<Long> platformIds, String sortType, Pageable pageable);


    @Query("SELECT g FROM Game g " +
            "LEFT JOIN g.categories c " +
            "LEFT JOIN g.platforms p " +
            "WHERE " +
            "(COALESCE(:keyword, '') = '' OR " +
            "g.name LIKE %:keyword% OR " +
            "g.describe LIKE %:keyword%) AND " +
            "(:categoryIds IS NULL OR c.id IN :categoryIds) AND " +
            "(:platformIds IS NULL OR p.id IN :platformIds) AND " +
            "(:minPrice IS NULL OR g.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR g.price <= :maxPrice) " +
            "ORDER BY CASE WHEN :sortType = 'desc' THEN g.price END DESC, " +
            "CASE WHEN :sortType = 'asc' THEN g.price END ASC")
    Page<Game> searchAndFilter2(String keyword, List<Long> categoryIds, List<Long> platformIds, String sortType, Double minPrice, Double maxPrice, Pageable pageable);


    Page<Game> findAllByOrderByPriceDesc(Pageable pageable);

    Page<Game> findAllByOrderByPriceAsc(Pageable pageable);
}
