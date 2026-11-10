package com.harness.selfcodingagent.agent;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

import static org.assertj.core.api.Assertions.assertThat;

class ConversationTest {

    @Test
    @DisplayName("Системный промпт попадает в промпт")
    void systemPromptIsIncludedInPrompt() {
        Conversation conversation = new Conversation("SYSTEM");
        conversation.addUserMessage("привет");

        assertThat(conversation.toPromptMessages()).hasSize(2);
    }

    @Test
    @DisplayName("Собирает промпт в правильном порядке")
    void buildsPromptInCorrectOrder() {
        Conversation conversation = new Conversation("SYSTEM");
        conversation.addUserMessage("вопрос");
        conversation.addAssistantMessage(new AssistantMessage("ответ"));

        assertThat(conversation.toPromptMessages())
            .extracting(Message::getMessageType)
            .containsExactly(MessageType.SYSTEM, MessageType.USER, MessageType.ASSISTANT);
    }


}
