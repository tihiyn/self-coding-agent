package com.harness.selfcodingagent.agent;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class Agent {
    private static final String SYSTEM_PROMPT = """
        You are Harness, a coding agent that works in local repository.
        Answer concisely.
        """;

    private final Conversation conversation = new Conversation(SYSTEM_PROMPT);
    private final ChatModel model;

    public Agent(ChatModel model) {
        this.model = model;
    }

    public String run (String usrMsg) {
        conversation.addUserMessage(usrMsg);
        AssistantMessage am = model.call(new Prompt(conversation.toPromptMessages())).getResult().getOutput();
        conversation.addAssistantMessage(am);
        return am.getText();
    }
}
