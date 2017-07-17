package com.example.sid.marwadishaadi.Notifications;

/**
 * Created by Sid on 02-Jun-17.
 */

public class NotificationsModel {

    String name,timeStamp;
    int number;
    boolean suggested,interestRec,interestAcc,msgRec,premMem,memExp,reminders,offers,bday;

    public NotificationsModel() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public NotificationsModel(String name, String timeStamp, int number, boolean suggested, boolean interestRec, boolean interestAcc, boolean msgRec, boolean premMem, boolean memExp, boolean reminders, boolean offers, boolean bday) {

        this.name = name;
        this.timeStamp = timeStamp;
        this.number = number;
        this.suggested = suggested;
        this.interestRec = interestRec;
        this.interestAcc = interestAcc;
        this.msgRec = msgRec;
        this.premMem = premMem;
        this.memExp = memExp;
        this.reminders = reminders;
        this.offers = offers;
        this.bday = bday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSuggested() {
        return suggested;
    }

    public void setSuggested(boolean suggested) {
        this.suggested = suggested;
    }

    public boolean isInterestRec() {
        return interestRec;
    }

    public void setInterestRec(boolean interestRec) {
        this.interestRec = interestRec;
    }

    public boolean isInterestAcc() {
        return interestAcc;
    }

    public void setInterestAcc(boolean interestAcc) {
        this.interestAcc = interestAcc;
    }

    public boolean isMsgRec() {
        return msgRec;
    }

    public void setMsgRec(boolean msgRec) {
        this.msgRec = msgRec;
    }

    public boolean isPremMem() {
        return premMem;
    }

    public void setPremMem(boolean premMem) {
        this.premMem = premMem;
    }

    public boolean isMemExp() {
        return memExp;
    }

    public void setMemExp(boolean memExp) {
        this.memExp = memExp;
    }

    public boolean isReminders() {
        return reminders;
    }

    public void setReminders(boolean reminders) {
        this.reminders = reminders;
    }

    public boolean isOffers() {
        return offers;
    }

    public void setOffers(boolean offers) {
        this.offers = offers;
    }

    public boolean isBday() {
        return bday;
    }

    public void setBday(boolean bday) {
        this.bday = bday;
    }
}
