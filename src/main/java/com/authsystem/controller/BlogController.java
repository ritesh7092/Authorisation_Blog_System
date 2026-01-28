package com.authsystem.controller;

import com.authsystem.model.Blog;
import com.authsystem.model.User;
import com.authsystem.payload.BlogPayloads.*;
import com.authsystem.repository.BlogRepository;
import com.authsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/blogs")
public class BlogController {
    @Autowired
    BlogRepository blogRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public List<BlogResponse> getAllBlogs() {
        return blogRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> createBlog(@RequestBody BlogRequest blogRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Blog blog = new Blog(blogRequest.title(), blogRequest.content(), currentUser);
        Blog savedBlog = blogRepository.save(blog);

        return ResponseEntity.ok(convertToResponse(savedBlog));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateBlog(@PathVariable Long id, @RequestBody BlogRequest blogRequest) {
        Blog blog = blogRepository.findById(id).orElse(null);
        if (blog == null) {
            return ResponseEntity.notFound().build();
        }

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        // Check ownership or Admin role
        boolean isOwner = blog.getAuthor().getEmail().equals(currentUserEmail);
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
             return ResponseEntity.status(403).body("Error: You are not authorized to edit this blog.");
        }

        blog.setTitle(blogRequest.title());
        blog.setContent(blogRequest.content());
        Blog updatedBlog = blogRepository.save(blog);

        return ResponseEntity.ok(convertToResponse(updatedBlog));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id) {
        if (!blogRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        blogRepository.deleteById(id);
        return ResponseEntity.ok("Blog deleted successfully!");
    }

    private BlogResponse convertToResponse(Blog blog) {
        return new BlogResponse(
                blog.getId(),
                blog.getTitle(),
                blog.getContent(),
                new AuthorResponse(blog.getAuthor().getId(), blog.getAuthor().getName()),
                blog.getCreatedAt(),
                blog.getUpdatedAt()
        );
    }
}
