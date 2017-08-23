package com.sid.marwadishaadi;

/**
 * Created by Sid on 01-Jul-17.
 */

public class Notif_Message {

    private String from;
    private String msg;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    private String fullname;

    public Notif_Message(String from, String msg, String fullname) {
        this.from = from;
        this.msg = msg;
        this.fullname = fullname;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Notif_Message(String from, String msg) {
        this.from = from;
        this.msg = msg;
    }
}
