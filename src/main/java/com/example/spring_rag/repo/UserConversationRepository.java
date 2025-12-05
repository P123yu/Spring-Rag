package com.example.spring_rag.repo;

import com.example.spring_rag.model.UserConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserConversationRepository extends JpaRepository<UserConversation, String> {
    // Find all chats for a specific user
    List<UserConversation> findByUserEmailOrderByCreatedAtDesc(String userEmail);

}