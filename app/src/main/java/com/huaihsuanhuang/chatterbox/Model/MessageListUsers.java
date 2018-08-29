package com.huaihsuanhuang.chatterbox.Model;

public class MessageListUsers {

    private String mChatUid;
    private String mChatUserName;
    private String mChatUserThumb;
    private String mChatUserOnline;
    private String mLastMsg;
    private String mTimestamp;


    public MessageListUsers(String mChatUid, String mChatUserName, String mChatUserThumb, String mChatUserOnline, String mLastMsg, String mTimestamp) {
        this.mChatUid = mChatUid;
        this.mChatUserName = mChatUserName;
        this.mChatUserThumb = mChatUserThumb;
        this.mChatUserOnline = mChatUserOnline;
        this.mLastMsg = mLastMsg;
        this.mTimestamp = mTimestamp;

    }

    public String getChatUid() {
        return mChatUid;
    }

    public void setChatUid(String mChatUid) {
        this.mChatUid = mChatUid;
    }

    public String getChatUserName() {
        return mChatUserName;
    }

    public void setChatUserName(String mChatUserName) {
        this.mChatUserName = mChatUserName;
    }

    public String getChatUserThumb() {
        return mChatUserThumb;
    }

    public void setChatUserThumb(String mChatUserThumb) {
        this.mChatUserThumb = mChatUserThumb;
    }

    public String getChatUserOnline() {
        return mChatUserOnline;
    }

    public void setChatUserOnline(String mChatUserOnline) {
        this.mChatUserOnline = mChatUserOnline;
    }

    public String getLastMsg() {
        return mLastMsg;
    }

    public void setLastMsg(String mLastMsg) {
        this.mLastMsg = mLastMsg;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(String mTimestamp) {
        this.mTimestamp = mTimestamp;
    }


}
