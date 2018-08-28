package com.huaihsuanhuang.chatterbox.Model;

public class MessageListUsers {

    String chat_userName;
    String chat_userThumb;
    String chat_userOnline;

    public MessageListUsers() {
    }

    public MessageListUsers(String chat_userName, String chat_userThumb, String chat_userOnline) {
        this.chat_userName = chat_userName;
        this.chat_userThumb = chat_userThumb;
        this.chat_userOnline = chat_userOnline;
    }

    public String getChat_userName() {
        return chat_userName;
    }

    public void setChat_userName(String chat_userName) {
        this.chat_userName = chat_userName;
    }

    public String getChat_userThumb() {
        return chat_userThumb;
    }

    public void setChat_userThumb(String chat_userThumb) {
        this.chat_userThumb = chat_userThumb;
    }

    public String getChat_userOnline() {
        return chat_userOnline;
    }

    public void setChat_userOnline(String chat_userOnline) {
        this.chat_userOnline = chat_userOnline;
    }
}
