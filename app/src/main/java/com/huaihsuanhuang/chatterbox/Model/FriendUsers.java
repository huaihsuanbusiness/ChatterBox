package com.huaihsuanhuang.chatterbox.Model;

public class FriendUsers {


    private String name;
    private String status;
    private String image;
    private String online;
    private String thumb_image;
    private String key;


    public FriendUsers(){}

    public FriendUsers(String name, String status, String image, String online, String thumb_image, String key) {
        this.name = name;
        this.status = status;
        this.image = image;
        this.online = online;
        this.thumb_image = thumb_image;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
