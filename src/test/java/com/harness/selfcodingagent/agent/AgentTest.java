package com.harness.selfcodingagent.agent;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AgentTest {

    @Test
    @DisplayName("Возвращает ответ модели")
    void returnsModelReply() {
        Agent agent = new Agent(ScriptedChatModel.replyingWith("I am Harness."));

        assertThat(agent.run("Кто ты?")).isEqualTo("I am Harness.");
    }

    @Test
    @DisplayName("Первым сообщением всегда идёт системный промпт")
    void systemPromptIsAlwaysFirstMessage() {
        ScriptedChatModel model = ScriptedChatModel.replyingWith("ok");
        Agent agent = new Agent(model);

        agent.run("Привет");

        List<Message> sent = model.lastPrompt().getInstructions();
        assertThat(sent.getFirst().getMessageType()).isEqualTo(MessageType.SYSTEM);
        assertThat(sent.getFirst().getText()).contains("Harness");
    }

    @Test
    @DisplayName("Помнит предыдущие ходы")
    void remembersPreviousTurns() {
        ScriptedChatModel model = ScriptedChatModel.replyingWith("Привет!", "Тебя зовут Саша.");
        Agent agent = new Agent(model);

        agent.run("Меня зовут Саша");
        agent.run("Как меня зовут?");

        List<String> secondCall = model.lastPrompt().getInstructions().stream().map(Message::getText).toList();
        assertThat(secondCall)
            .containsExactly(secondCall.getFirst(), "Меня зовут Саша", "Привет!", "Как меня зовут?");
    }
}
