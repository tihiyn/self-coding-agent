package com.harness.selfcodingagent.agent;

import org.jspecify.annotations.NullMarked;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Мок-модель со сценарием: отдаёт заранее заданные ответы по очереди
 * и запоминает, какие промпты получила.
 */
@NullMarked
public class ScriptedChatModel implements ChatModel {

    private final Deque<AssistantMessage> script = new ArrayDeque<>();
    private final List<Prompt> receivedPrompts = new ArrayList<>();

    public static ScriptedChatModel replyingWith(String... texts) {
        ScriptedChatModel model = new ScriptedChatModel();
        for (String text : texts) {
            model.script.add(new AssistantMessage(text));
        }
        return model;
    }

    @Override
    public ChatResponse call(Prompt prompt) {
        receivedPrompts.add(prompt);
        AssistantMessage next = script.poll();
        if (next == null) {
            throw new IllegalStateException(
                    "Сценарий исчерпан: агент сделал больше вызовов модели, чем ожидалось (%d)"
                            .formatted(receivedPrompts.size()));
        }
        return new ChatResponse(List.of(new Generation(next)));
    }

    public Prompt lastPrompt() {
        return receivedPrompts.getLast();
    }

}
