package com.huaihsuanhuang.chatterbox.Model;

public class Seen {


    public String seen;
    public long timestamp;

    public Seen() {

    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Seen(String seen, long timestamp) {
        this.seen = seen;
        this.timestamp = timestamp;
    }
}
