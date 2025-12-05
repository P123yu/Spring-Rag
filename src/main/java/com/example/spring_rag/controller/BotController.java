//package com.example.spring_rag.controller;
//
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.chat.prompt.SystemPromptTemplate;
//import org.springframework.ai.chat.messages.UserMessage;
//import org.springframework.ai.document.Document;
//import org.springframework.ai.reader.tika.TikaDocumentReader;
//import org.springframework.ai.vectorstore.SearchRequest;
//import org.springframework.ai.vectorstore.VectorStore;
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@RestController
//public class BotController {
//
//    private final VectorStore vectorStore;
//    private final ChatClient chatClient;
//
//    public BotController(VectorStore vectorStore, ChatClient.Builder chatClientBuilder) {
//        this.vectorStore = vectorStore;
//        this.chatClient = chatClientBuilder.build();
//    }
//
//    // 1. INGESTION ENDPOINT
//    // Upload a file (PDF/TXT) to store in the database
//    @PostMapping("/")
//    public String ingest(@RequestParam("file") MultipartFile file) throws IOException {
//        // Use Tika to read any document type
//        TikaDocumentReader reader = new TikaDocumentReader(new InputStreamResource(file.getInputStream()));
//        List<Document> documents = reader.get();
//
//        // Add metadata (filename) if needed
//        documents.forEach(doc -> doc.getMetadata().put("filename", file.getOriginalFilename()));
//
//        // Save to PgVector (This automatically calls OpenAI to get embeddings)
//        vectorStore.add(documents);
//
//        return "Document ingested successfully!";
//    }
//
//    // 2. RAG CHAT ENDPOINT
//    // Ask a question about the uploaded document
//    @GetMapping("/search")
//    public String search(@RequestParam String query) {
//        // A. RETRIEVE: Find the top 3 most similar document chunks
//        List<Document> similarDocs = // NEW (Spring AI 1.0+)
//                vectorStore.similaritySearch(
//                        SearchRequest.builder()
//                                .query(query)
//                                .topK(3)
//                                .build()
//                );
//
//        // B. AUGMENT: Convert docs to a single string to put in the prompt
//        String information = similarDocs.stream()
//                .map(doc -> "Title: " + doc.getMetadata().get("filename") + "\nBody: " + doc.getText())
//                .collect(Collectors.joining("\n---\n"));
//
//        // C. GENERATE: Create the system prompt with the context
////        String systemPromptText = """
////                You are a helpful assistant.
////                You find information in the data provided below.
////                If there is no information, say "Sorry, I do not have such information".
////
////                DATA:
////                {information}
////                """;
//
//        String systemPromptText = """
//    You are a professional documentation assistant.
//    Your task is to answer the user's question based STRICTLY on the provided context.
//
//    Rules:
//    1. Use ONLY the information provided in the DATA section below. Do not use your own training data.
//    2. If the answer cannot be found in the DATA, do not make something up. Instead, reply: "I am sorry, I do not have such information."
//    3. Keep your answer concise, accurate, and to the point.
//    4. Do not mention that you are referencing "DATA" or "documents" in your final answer; just provide the facts.
//
//    DATA:
//    {information}
//    """;
//
//
////        String systemPromptText = """
////    You are an expert analyst. Answer the user's question using the context provided below.
////
////    Guidelines:
////    - Structure your answer using Markdown (headers, bold text, lists).
////    - If the context contains multiple relevant points, use a bulleted list.
////    - Reference the 'Title' of the source document where you found the information.
////    - If the information is partial or incomplete, mention what is missing.
////    - If the context is irrelevant to the question, state: "The provided context is not relevant to your query."
////
////    CONTEXT DATA:
////    {information}
////    """;
//
//        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPromptText);
//        var systemMessage = systemPromptTemplate.createMessage(Map.of("information", information));
//
//        // Create the user message
//        var userMessage = new UserMessage(query);
//
//        // D. CALL AI: Send everything to OpenAI
//        return chatClient.prompt(new Prompt(List.of(systemMessage, userMessage)))
//                .call()
//                .content();
//    }
//}

//
//package com.example.spring_rag.controller;
//
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
//import org.springframework.ai.chat.memory.ChatMemory;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.chat.prompt.SystemPromptTemplate;
//import org.springframework.ai.chat.messages.UserMessage;
//import org.springframework.ai.document.Document;
//import org.springframework.ai.reader.tika.TikaDocumentReader;
//import org.springframework.ai.vectorstore.SearchRequest;
//import org.springframework.ai.vectorstore.VectorStore;
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@RestController
//public class BotController {
//
//    private final VectorStore vectorStore;
//    private final ChatClient chatClient;
//
//    // We rely on Spring to inject the JDBC ChatMemory bean
//    public BotController(VectorStore vectorStore,
//                         ChatClient.Builder chatClientBuilder,
//                         ChatMemory chatMemory) {
//
//        this.vectorStore = vectorStore;
//
//        this.chatClient = chatClientBuilder
//                .defaultAdvisors(
//                        // Use the Builder to avoid "Private Access" errors
//                        MessageChatMemoryAdvisor.builder(chatMemory).build()
//                )
//                .build();
//    }
//
//
//    @PostMapping("/")
//    public String ingest(@RequestParam("file") MultipartFile file) throws IOException {
//        TikaDocumentReader reader = new TikaDocumentReader(new InputStreamResource(file.getInputStream()));
//        List<Document> documents = reader.get();
//        documents.forEach(doc -> doc.getMetadata().put("filename", file.getOriginalFilename()));
//        vectorStore.add(documents);
//        return "Document ingested successfully!";
//    }
//
//    @GetMapping("/search")
//    public String search(@RequestParam String query, @RequestParam(defaultValue = "default") String chatId) {
//
//        // A. RAG RETRIEVAL
//        List<Document> similarDocs = vectorStore.similaritySearch(
//                SearchRequest.builder().query(query).topK(3).build()
//        );
//
//        String information = similarDocs.stream()
//                .map(doc -> "Title: " + doc.getMetadata().get("filename") + "\nBody: " + doc.getText())
//                .collect(Collectors.joining("\n---\n"));
//
//        // B. SYSTEM PROMPT
//        String systemPromptText = """
//            You are a professional documentation assistant.
//            Rules:
//            1. Use the information provided in the DATA section below.
//            2. If the answer is in the chat history, use that too.
//            DATA:
//            {information}
//            """;
//
//        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPromptText);
//        var systemMessage = systemPromptTemplate.createMessage(Map.of("information", information));
//
//        // C. CALL AI WITH MEMORY
//        return chatClient.prompt()
//                .system(systemMessage.getText())
//                .user(query)
//                .advisors(a -> a
//                        // FIX: Use String literals directly to avoid Import error
//                        .param("chat_memory_conversation_id", chatId)
//                        .param("chat_memory_retrieve_size", 10))
//                .call()
//                .content();
//    }
//}



package com.example.spring_rag.controller;

import com.example.spring_rag.model.UserConversation;
import com.example.spring_rag.repo.UserConversationRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class BotController {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;
    private final UserConversationRepository conversationRepository;

    public BotController(VectorStore vectorStore,
                         ChatClient.Builder chatClientBuilder,
                         ChatMemory chatMemory,
                         UserConversationRepository conversationRepository) {

        this.vectorStore = vectorStore;
        this.conversationRepository = conversationRepository;
        this.chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    // --- NEW ENDPOINT: START A NEW CHAT ---
    // React calls this when user clicks "New Chat"
    // Returns a UUID that represents the chat ID
    @PostMapping("/chat/new")
    public String createNewChat(@RequestParam String userEmail, @RequestParam(defaultValue = "New Chat") String title) {
        String newChatId = UUID.randomUUID().toString(); // Generate unique ID

        UserConversation conversation = new UserConversation(
                newChatId,
                userEmail,
                title,
                LocalDateTime.now()
        );

        conversationRepository.save(conversation);
        return newChatId;
    }

    // --- NEW ENDPOINT: LIST USER'S CHATS ---
    // React uses this to show the sidebar history
    @GetMapping("/chat/history")
    public List<UserConversation> getUserChats(@RequestParam String userEmail) {
        return conversationRepository.findByUserEmailOrderByCreatedAtDesc(userEmail);
    }

    // --- UPDATED SEARCH ENDPOINT ---
    @GetMapping("/search")
    public String search(@RequestParam String query,
                         @RequestParam String chatId,
                         @RequestParam String userEmail) { // Need email to verify ownership

        // 1. SECURITY CHECK
        // Check if this chatId actually belongs to this userEmail
        UserConversation conversation = conversationRepository.findById(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat not found"));

        if (!conversation.getUserEmail().equals(userEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this chat conversation");
        }

        // 2. EXISTING RAG LOGIC
        List<Document> similarDocs = vectorStore.similaritySearch(
                SearchRequest.builder().query(query).topK(3).build()
        );

        String information = similarDocs.stream()
                .map(doc -> "Title: " + doc.getMetadata().get("filename") + "\nBody: " + doc.getText())
                .collect(Collectors.joining("\n---\n"));

        String systemPromptText = """
            You are a helpful assistant.
            Use the information provided in the DATA section below.
            Also use the previous CHAT HISTORY to understand the context.
            DATA:
            {information}
            """;

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPromptText);
        var systemMessage = systemPromptTemplate.createMessage(Map.of("information", information));

        // 3. CALL AI WITH MEMORY (Using the validated chatId)
        return chatClient.prompt()
                .system(systemMessage.getText())
                .user(query)
                .advisors(a -> a
                        .param("chat_memory_conversation_id", chatId)
                        .param("chat_memory_retrieve_size", 10))
                .call()
                .content();
    }
}