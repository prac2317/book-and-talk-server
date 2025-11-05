package com.talk.book.repository;

import com.talk.book.domain.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findByIsbn13(String isbn13);

    @Query(value = "SELECT c.* FROM club c " +
            "WHERE ST_DWithin(c.location, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326), :radiusInMeters) " +
            "ORDER BY ST_Distance(c.location, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326))",
            nativeQuery = true)
    Page<Club> findNearbyClubs(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radiusInMeters") double radiusInMeters,
            Pageable pageable
    );
}
