package com.example.zorker.vivaha.Chat;

public class Messages {

    private String message_body;
    private String message_type;
    private boolean message_seen;
    private long message_time;
    private String message_from;

    public Messages(String message_body, String message_from, String message_type, boolean message_seen, long message_time) {
        this.message_body = message_body;
        this.message_from = message_from;
        this.message_type = message_type;
        this.message_seen = message_seen;
        this.message_time = message_time;
    }

    public Messages()
    {}



    public String getMessage_body() {
        return message_body;
    }

    public void setMessage_body(String message_body) {
        this.message_body = message_body;
    }

    public String getMessage_from() {
        return message_from;
    }

    public void setMessage_from(String message_from) {
        this.message_from = message_from;
    }

    public boolean isMessage_seen() {
        return message_seen;
    }

    public void setMessage_seen(boolean message_seen) {
        this.message_seen = message_seen;
    }

    public long getMessage_time() {
        return message_time;
    }

    public void setMessage_time(long message_time) {
        this.message_time = message_time;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }
}
