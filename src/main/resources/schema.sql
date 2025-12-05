

--
--DROP TABLE IF EXISTS spring_ai_chat_memory;
--
--CREATE TABLE spring_ai_chat_memory (
--    -- FIX: Auto-generate the ID because Spring AI doesn't send one
--    message_id varchar(255) NOT NULL DEFAULT gen_random_uuid()::text,
--    conversation_id varchar(255) NOT NULL,
--    content text NOT NULL,
--    type varchar(255) NOT NULL,
--    metadata text,
--    timestamp timestamp DEFAULT CURRENT_TIMESTAMP,
--    PRIMARY KEY (message_id)
--);
--
--CREATE INDEX IF NOT EXISTS idx_spring_ai_chat_memory_conv_id ON spring_ai_chat_memory (conversation_id);





-- 1. Spring AI Chat Memory Table (For storing messages)
DROP TABLE IF EXISTS spring_ai_chat_memory;

CREATE TABLE spring_ai_chat_memory (
    message_id varchar(255) NOT NULL DEFAULT gen_random_uuid()::text,
    conversation_id varchar(255) NOT NULL,
    content text NOT NULL,
    type varchar(255) NOT NULL,
    metadata text,
    timestamp timestamp DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (message_id)
);

CREATE INDEX IF NOT EXISTS idx_spring_ai_chat_memory_conv_id ON spring_ai_chat_memory (conversation_id);

-- 2. User Conversations Table (For mapping Users to Chat IDs)
CREATE TABLE IF NOT EXISTS user_conversations (
    conversation_id varchar(255) NOT NULL,
    user_email varchar(255) NOT NULL,
    title varchar(255),
    created_at timestamp,
    PRIMARY KEY (conversation_id)
);