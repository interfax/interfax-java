package net.interfax.rest.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InboundFaxesEmailsStructure {

    private String emailAddress;
    private int messageStatus;
    private Date completionTime;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(final int messageStatus) {
        this.messageStatus = messageStatus;
    }

    public Date getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(final Date completionTime) {
        this.completionTime = completionTime;
    }
}
