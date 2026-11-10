package com.harness.selfcodingagent.agent;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    private final String systemPrompt;
    private final List<Message> msgs = new ArrayList<>();

    public Conversation(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }

    public void addUserMessage(String um) {
        msgs.add(new UserMessage(um));
    }

    public void addAssistantMessage(AssistantMessage am) {
        msgs.add(am);
    }

    public List<Message> toPromptMessages() {
        List<Message> prompt = new ArrayList<>(msgs.size() + 1);
        prompt.add(new SystemMessage(systemPrompt));
        prompt.addAll(msgs);
        return prompt;
    }
}
