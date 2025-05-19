package com.grepp.synapse4.app.controller.web.bookmark;

import com.grepp.synapse4.app.model.user.BookmarkService;
import com.grepp.synapse4.app.model.user.dto.CustomUserDetails;
import com.grepp.synapse4.app.model.user.dto.MyBookMarkDto;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("bookmarks")
@RequiredArgsConstructor
public class BookMarkController {

    private final BookmarkService bookmarkService;

    @GetMapping(value = "")
    public String bookmarks(
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws NotFoundException {
        Long userId = userDetails.getUser().getId();
        List<MyBookMarkDto> bookmarks = bookmarkService.findByBookmarkId(userId);
        model.addAttribute("bookmarks", bookmarks);
        return "bookmarks/bookmarks";
    }



}
