package com.huaihsuanhuang.chatterbox.Model;

public class Seen {


    public boolean seen;
    public long timestamp;

    public Seen() {

    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Seen(boolean seen, long timestamp) {
        this.seen = seen;
        this.timestamp = timestamp;
    }
}
