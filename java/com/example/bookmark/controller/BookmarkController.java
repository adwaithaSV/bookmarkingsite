package com.example.bookmark.controller;

import com.example.bookmark.model.Bookmark;
import com.example.bookmark.model.User;
import com.example.bookmark.service.BookmarkService;
import com.example.bookmark.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final UserService userService;

    public BookmarkController(BookmarkService bookmarkService, UserService userService) {
        this.bookmarkService = bookmarkService;
        this.userService = userService;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username).orElse(null);
    }

    @GetMapping
    public String listBookmarks(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "addedTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String searchTerm) {

        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Bookmark> bookmarkPage;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            bookmarkPage = bookmarkService.searchUserBookmarks(currentUser, searchTerm.trim(), pageable);
        } else {
            bookmarkPage = bookmarkService.getUserBookmarks(currentUser, pageable);
        }

        model.addAttribute("bookmarkPage", bookmarkPage);
        model.addAttribute("currentPage", bookmarkPage.getNumber());
        model.addAttribute("pageSize", bookmarkPage.getSize());
        model.addAttribute("totalPages", bookmarkPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("searchTerm", searchTerm);

        return "bookmark-list";
    }

    @GetMapping("/add")
    public String showAddBookmarkForm(Model model) {
        if (!model.containsAttribute("bookmark")) {
            model.addAttribute("bookmark", new Bookmark());
        }
        return "bookmark-form";
    }

    @PostMapping("/add")
    public String addBookmark(@Valid @ModelAttribute Bookmark bookmark,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You must be logged in to add bookmarks.");
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please correct the errors below.");
            redirectAttributes.addFlashAttribute("bookmark", bookmark);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.bookmark", result);
            return "redirect:/bookmarks/add";
        }

        bookmark.setUser(currentUser);
        bookmark.setAddedTime(LocalDateTime.now());

        try {
            bookmarkService.addBookmark(bookmark);
            redirectAttributes.addFlashAttribute("successMessage", "Bookmark added successfully!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("bookmark", bookmark);
            return "redirect:/bookmarks/add";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding bookmark: " + e.getMessage());
            redirectAttributes.addFlashAttribute("bookmark", bookmark);
            return "redirect:/bookmarks/add";
        }
        return "redirect:/bookmarks";
    }

    @GetMapping("/edit/{id}")
    public String showEditBookmarkForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You must be logged in to edit bookmarks.");
            return "redirect:/login";
        }

        Optional<Bookmark> optionalBookmark = bookmarkService.getBookmarkById(id);
        if (optionalBookmark.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bookmark not found.");
            return "redirect:/bookmarks";
        }

        Bookmark bookmark = optionalBookmark.get();
        if (!bookmark.getUser().getId().equals(currentUser.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to edit this bookmark.");
            return "redirect:/bookmarks";
        }

        if (!model.containsAttribute("bookmark")) {
            model.addAttribute("bookmark", bookmark);
        }
        return "bookmark-form";
    }

    @PostMapping("/edit/{id}")
    public String editBookmark(@PathVariable Long id,
                               @Valid @ModelAttribute Bookmark bookmark,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You must be logged in to edit bookmarks.");
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please correct the errors below.");
            redirectAttributes.addFlashAttribute("bookmark", bookmark);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.bookmark", result);
            return "redirect:/bookmarks/edit/" + id;
        }

        Optional<Bookmark> existingBookmarkOpt = bookmarkService.getBookmarkById(id);
        if (existingBookmarkOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bookmark not found.");
            return "redirect:/bookmarks";
        }

        Bookmark existingBookmark = existingBookmarkOpt.get();
        if (!existingBookmark.getUser().getId().equals(currentUser.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to edit this bookmark.");
            return "redirect:/bookmarks";
        }

        existingBookmark.setTitle(bookmark.getTitle());
        existingBookmark.setUrl(bookmark.getUrl());

        try {
            bookmarkService.updateBookmark(existingBookmark);
            redirectAttributes.addFlashAttribute("successMessage", "Bookmark updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating bookmark: " + e.getMessage());
            redirectAttributes.addFlashAttribute("bookmark", bookmark);
            return "redirect:/bookmarks/edit/" + id;
        }
        return "redirect:/bookmarks";
    }

    @PostMapping("/delete/{id}")
    public String deleteBookmark(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "You must be logged in to delete bookmarks.");
            return "redirect:/login";
        }

        Optional<Bookmark> optionalBookmark = bookmarkService.getBookmarkById(id);
        if (optionalBookmark.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bookmark not found.");
            return "redirect:/bookmarks";
        }

        Bookmark bookmark = optionalBookmark.get();
        if (!bookmark.getUser().getId().equals(currentUser.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to delete this bookmark.");
            return "redirect:/bookmarks";
        }

        try {
            bookmarkService.deleteBookmark(id);
            redirectAttributes.addFlashAttribute("successMessage", "Bookmark deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting bookmark: " + e.getMessage());
        }
        return "redirect:/bookmarks";
    }
}