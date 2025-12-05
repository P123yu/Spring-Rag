package com.example.spring_rag.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_conversations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserConversation {

    @Id
    private String conversationId; // The UUID used by Spring AI

    private String userEmail;      // The owner
    private String title;          // e.g., "Chat about Java"
    private LocalDateTime createdAt;
}