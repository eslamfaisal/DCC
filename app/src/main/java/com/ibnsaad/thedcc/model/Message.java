package com.ibnsaad.thedcc.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Message implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("senderId")
    @Expose
    private String senderId;
    @SerializedName("senderKnownAs")
    @Expose
    private String senderKnownAs;
    @SerializedName("senderPhotoUrl")
    @Expose
    private String senderPhotoUrl;
    @SerializedName("recipientPhotoUrl")
    @Expose
    private Object recipientPhotoUrl;
    @SerializedName("recipientId")
    @Expose
    private String recipientId;
    @SerializedName("recipientKnownAs")
    @Expose
    private String recipientKnownAs;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("isRead")
    @Expose
    private Boolean isRead;
    @SerializedName("dateRead")
    @Expose
    private Object dateRead;
    @SerializedName("messageSent")
    @Expose
    private String messageSent;


    public Message(String  senderId, String recipientId,
                   String messageSent, String content ) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.content = content;
        this.messageSent = messageSent;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderKnownAs() {
        return senderKnownAs;
    }

    public void setSenderKnownAs(String senderKnownAs) {
        this.senderKnownAs = senderKnownAs;
    }

    public String getSenderPhotoUrl() {
        return senderPhotoUrl;
    }

    public void setSenderPhotoUrl(String senderPhotoUrl) {
        this.senderPhotoUrl = senderPhotoUrl;
    }

    public Object getRecipientPhotoUrl() {
        return recipientPhotoUrl;
    }

    public void setRecipientPhotoUrl(Object recipientPhotoUrl) {
        this.recipientPhotoUrl = recipientPhotoUrl;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientKnownAs() {
        return recipientKnownAs;
    }

    public void setRecipientKnownAs(String recipientKnownAs) {
        this.recipientKnownAs = recipientKnownAs;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Object getDateRead() {
        return dateRead;
    }

    public void setDateRead(Object dateRead) {
        this.dateRead = dateRead;
    }

    public String getMessageSent() {
        return messageSent;
    }

    public void setMessageSent(String messageSent) {
        this.messageSent = messageSent;
    }

}