package com.repository;

import com.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    // Hoặc sử dụng @Query annotation để viết truy vấn SQL tương tự
    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.rating = :rating AND f.game.id = :gameId")
    int countByRatingAndProductId(
            @Param("rating") long rating,
            @Param("gameId") long productId
    );

    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.game.id = :gameId")
    int countFeedbacksByProductId(@Param("gameId") long gameId);
}
