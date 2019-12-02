package com.chatapp.database.Entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class MessageTable {

    @PrimaryKey
    long message_id;
    String channel_url;
    long message_ts;
    byte[] data;

    public MessageTable(long message_id, String channel_url, long message_ts, byte[] data) {
        this.message_id = message_id;
        this.channel_url = channel_url;
        this.message_ts = message_ts;
        this.data = data;
    }

    public long getMessage_id() {
        return message_id;
    }

    public void setMessage_id(long message_id) {
        this.message_id = message_id;
    }

    public String getChannel_url() {
        return channel_url;
    }

    public void setChannel_url(String channel_url) {
        this.channel_url = channel_url;
    }

    public long getMessage_ts() {
        return message_ts;
    }

    public void setMessage_ts(long message_ts) {
        this.message_ts = message_ts;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
