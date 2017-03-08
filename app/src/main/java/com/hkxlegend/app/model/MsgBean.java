package com.hkxlegend.app.model;

/**
 * @author huangkexiang
 * @since 17/3/3
 */

public class MsgBean {
    private String msg;
    private boolean isMsgFromMe;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isMsgFromMe() {
        return isMsgFromMe;
    }

    public void setMsgFromMe(boolean msgFromMe) {
        isMsgFromMe = msgFromMe;
    }
}
