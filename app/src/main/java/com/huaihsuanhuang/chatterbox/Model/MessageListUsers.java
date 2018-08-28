package com.huaihsuanhuang.chatterbox.Model;

public class MessageListUsers {

    private String mChatUid;
    private String mChatUserName;
    private String mChatUserThumb;
    private String mChatUserOnline;
    private String mLastMsg;
    private String mTimestamp;

    public MessageListUsers(String chatUid, String chatUserName, String chatUserThumb, String chatUserOnline, String lastMsg, String timestamp) {
        mChatUid = chatUid;
        mLastMsg = lastMsg;
        mTimestamp = timestamp;
        mChatUserName = chatUserName;
        mChatUserThumb = chatUserThumb;
        mChatUserOnline = chatUserOnline;
    }

    public String getChatUid() {
        return mChatUid;
    }

    public String getLastMsg() {
        return mLastMsg;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public String getChatUserName() {
        return mChatUserName;
    }

    public String getChatUserThumb() {
        return mChatUserThumb;
    }

    public String getChatUserOnline() {
        return mChatUserOnline;
    }

}
