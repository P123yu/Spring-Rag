//package com.example.spring_rag.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Entity
//@Table(name = "message_store",
//        indexes = {
//                @Index(name = "message_store_conversation_id_idx", columnList = "conversation_id")
//        })
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//
//public class MessageStore {
//
//    @Id
//    @Column(name = "message_id", length = 255, nullable = false)
//    private String messageId;
//
//    @Column(name = "conversation_id", length = 255, nullable = false)
//    private String conversationId;
//
//    @Column(name = "message_type", length = 255, nullable = false)
//    private String messageType;
//
//    @Column(name = "message_content", columnDefinition = "text")
//    private String messageContent;
//
//    @Column(name = "message_metadata", columnDefinition = "text")
//    private String messageMetadata;
//}
