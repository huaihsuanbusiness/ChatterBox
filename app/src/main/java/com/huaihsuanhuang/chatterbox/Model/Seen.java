package com.huaihsuanhuang.chatterbox.Model;

public class Seen {


    public String seen;
    public String timestamp;

    public Seen() {

    }

    public Seen(String seen, String timestamp) {
        this.seen = seen;
        this.timestamp = timestamp;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
