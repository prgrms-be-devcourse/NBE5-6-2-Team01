package com.grepp.synapse4.app.model.user;

import com.grepp.synapse4.app.model.user.entity.Bookmark;
import com.grepp.synapse4.app.model.user.repository.BookMarkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BookMarkService {

  private final BookMarkRepository bookmarkRepository;

  public List<Bookmark> findByUserId(Long userId) {
    return bookmarkRepository.findAllByUserId(userId);
  }
}
