package com.authsystem.payload;

import java.time.LocalDateTime;

public class BlogPayloads {
    public record BlogRequest(String title, String content) {}

    public record AuthorResponse(Long id, String name) {}

    public record BlogResponse(Long id, String title, String content, AuthorResponse author, LocalDateTime createdAt, LocalDateTime updatedAt) {}
}
