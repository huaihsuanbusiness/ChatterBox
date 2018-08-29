package com.huaihsuanhuang.chatterbox.Model;

public class Requestsmodel {
    public String request_type;

    public Requestsmodel() {

    }

    public Requestsmodel(String request_type) {
        this.request_type = request_type;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }
}
