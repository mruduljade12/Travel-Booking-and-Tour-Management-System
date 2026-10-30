package com.mrudul.tourandtravel.user.user_models;

/**
 * ChatMessage — represents a single message in the AI assistant conversation.
 * TYPE_USER = message sent by the user (right-aligned blue bubble)
 * TYPE_BOT  = response from the AI assistant (left-aligned white bubble)
 */
public class ChatMessage {
    public static final int TYPE_USER = 0;
    public static final int TYPE_BOT  = 1;

    private final String text;
    private final int    type;       // TYPE_USER or TYPE_BOT
    private final String timestamp;

    public ChatMessage(String text, int type, String timestamp) {
        this.text      = text;
        this.type      = type;
        this.timestamp = timestamp;
    }

    public String getText()      { return text; }
    public int    getType()      { return type; }
    public String getTimestamp() { return timestamp; }
}