//package com.example.spring_rag.config;
//
//import org.springframework.ai.chat.memory.ChatMemory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class ChatMemoryConfig {
//
//    @Bean
//    public ChatMemory chatMemory(DataSource dataSource) {
//        return new JdbcChatMemory(dataSource, "message_store");
//    }
//}
