package com.example.vizsgaremek.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    List<Review> findByGameId(long gameId);
    @Query("select distinct r from Review r left join fetch r.game g where g.id =:id and r.username=:username")
    Optional<Review> findReviewByUsername(@Param("id") long id, @Param("username")String username);

    void deleteReviewsByGame_Id(long id);

}
