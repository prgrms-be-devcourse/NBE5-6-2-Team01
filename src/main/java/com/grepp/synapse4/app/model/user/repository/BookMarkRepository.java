package com.grepp.synapse4.app.model.user.repository;


import com.grepp.synapse4.app.model.user.dto.BookMarkDto;
import com.grepp.synapse4.app.model.user.entity.Bookmark;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMarkRepository extends JpaRepository<Bookmark, Long> {


    @Query("""
      SELECT b
      FROM Bookmark b
      JOIN FETCH b.restaurant r
      WHERE b.user.id = :userId
      ORDER BY b.createdAt DESC
    """)
    List<Bookmark> findByUserId(Long userId);

}


