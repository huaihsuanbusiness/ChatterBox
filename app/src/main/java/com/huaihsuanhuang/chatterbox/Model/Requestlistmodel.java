package com.huaihsuanhuang.chatterbox.Model;

public class Requestlistmodel {

    private String name;
    private String thumb_image;
    private String userid;
    public Requestlistmodel(){


    }

    public Requestlistmodel(String name, String thumb_image, String userid) {
        this.name = name;
        this.thumb_image = thumb_image;
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
