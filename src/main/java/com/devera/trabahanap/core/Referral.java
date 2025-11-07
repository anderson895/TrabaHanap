package com.devera.trabahanap.core;

import javafx.beans.property.*;

/**
 * Referral model using JavaFX properties for UI binding.
 * Fields:
 *  - referralId: unique Firestore document id
 *  - referredByUserId: ID of the user who sent the referral
 *  - referredEmail: email of the person being referred
 *  - referredName: name of the referred person
 *  - jobId: job associated with the referral (if any)
 *  - status: pending / accepted / rejected
 *  - timestamp: when the referral was made
 */
public class Referral {

    private final StringProperty referralId = new SimpleStringProperty();
    private final StringProperty referredByUserId = new SimpleStringProperty();
    private final StringProperty referredEmail = new SimpleStringProperty();
    private final StringProperty referredName = new SimpleStringProperty();
    private final StringProperty jobId = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final LongProperty timestamp = new SimpleLongProperty();

    public Referral() {}

    public Referral(String referralId,
                    String referredByUserId,
                    String referredEmail,
                    String referredName,
                    String jobId,
                    String status,
                    long timestamp) {
        this.referralId.set(referralId);
        this.referredByUserId.set(referredByUserId);
        this.referredEmail.set(referredEmail);
        this.referredName.set(referredName);
        this.jobId.set(jobId);
        this.status.set(status);
        this.timestamp.set(timestamp);
    }

    // --- Getters, Setters, and Property Methods ---
    public String getReferralId() { return referralId.get(); }
    public void setReferralId(String value) { referralId.set(value); }
    public StringProperty referralIdProperty() { return referralId; }

    public String getReferredByUserId() { return referredByUserId.get(); }
    public void setReferredByUserId(String value) { referredByUserId.set(value); }
    public StringProperty referredByUserIdProperty() { return referredByUserId; }

    public String getReferredEmail() { return referredEmail.get(); }
    public void setReferredEmail(String value) { referredEmail.set(value); }
    public StringProperty referredEmailProperty() { return referredEmail; }

    public String getReferredName() { return referredName.get(); }
    public void setReferredName(String value) { referredName.set(value); }
    public StringProperty referredNameProperty() { return referredName; }

    public String getJobId() { return jobId.get(); }
    public void setJobId(String value) { jobId.set(value); }
    public StringProperty jobIdProperty() { return jobId; }

    public String getStatus() { return status.get(); }
    public void setStatus(String value) { status.set(value); }
    public StringProperty statusProperty() { return status; }

    public long getTimestamp() { return timestamp.get(); }
    public void setTimestamp(long value) { timestamp.set(value); }
    public LongProperty timestampProperty() { return timestamp; }

    @Override
    public String toString() {
        return "Referral{" +
                "referralId='" + getReferralId() + '\'' +
                ", referredByUserId='" + getReferredByUserId() + '\'' +
                ", referredEmail='" + getReferredEmail() + '\'' +
                ", referredName='" + getReferredName() + '\'' +
                ", jobId='" + getJobId() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", timestamp=" + getTimestamp() +
                '}';
    }
}
