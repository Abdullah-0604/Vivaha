package com.example.zorker.vivaha.Chat;

public class ConversationModalClass {

    private Boolean message_seen;
    private Long time_stamp;

    public ConversationModalClass()
    {}

    public Boolean getMessage_seen() {
        return message_seen;
    }

    public void setMessage_seen(Boolean message_seen) {
        this.message_seen = message_seen;
    }

    public Long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Long time_stamp) {
        this.time_stamp = time_stamp;
    }
}
