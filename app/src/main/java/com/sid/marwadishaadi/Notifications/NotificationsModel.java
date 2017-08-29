package com.sid.marwadishaadi.Notifications;

/**
 * Created by Sid on 02-Jun-17.
 */

public class NotificationsModel {

    String name, timeStamp, id;
    int number;
    boolean suggested, interestRec, interestAcc, msgRec, premMem, memExp, reminders, offers, bday, isRead;

    public NotificationsModel() {
    }

    public NotificationsModel(String id, String name, String timeStamp, int number, boolean suggested, boolean interestRec, boolean interestAcc, boolean msgRec, boolean premMem, boolean memExp, boolean reminders, boolean offers, boolean bday, boolean isRead) {

        this.id = id;
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
        this.isRead = isRead;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
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
