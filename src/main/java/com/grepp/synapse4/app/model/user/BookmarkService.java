package com.grepp.synapse4.app.model.user;


import com.grepp.synapse4.app.model.user.entity.Bookmark;
import com.grepp.synapse4.app.model.user.repository.BookMarkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookMarkRepository bookMarkRepository;

    public BookmarkService(BookMarkRepository bookMarkRepository) {
        this.bookMarkRepository = bookMarkRepository;
    }

    public List<Bookmark> findByUserId(Long userId) {
        return bookMarkRepository.findByUserId(userId);
    }






}
