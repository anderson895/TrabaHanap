package com.devera.trabahanap.core;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.LongProperty;

public class Referral {

    private final StringProperty referralId;
    private final StringProperty referrerUserId;
    private final StringProperty referredUserId;
    private final StringProperty referralCode;
    private final LongProperty timestamp;

    public Referral() {
        this(null, null, null, null, 0L);
    }

    public Referral(String referralId, String referrerUserId, String referredUserId, String referralCode, long timestamp) {
        this.referralId = new SimpleStringProperty(referralId);
        this.referrerUserId = new SimpleStringProperty(referrerUserId);
        this.referredUserId = new SimpleStringProperty(referredUserId);
        this.referralCode = new SimpleStringProperty(referralCode);
        this.timestamp = new SimpleLongProperty(timestamp);
    }

    // --- referralId ---
    public String getReferralId() { return referralId.get(); }
    public void setReferralId(String value) { referralId.set(value); }
    public StringProperty referralIdProperty() { return referralId; }

    // --- referrerUserId ---
    public String getReferrerUserId() { return referrerUserId.get(); }
    public void setReferrerUserId(String value) { referrerUserId.set(value); }
    public StringProperty referrerUserIdProperty() { return referrerUserId; }

    // --- referredUserId ---
    public String getReferredUserId() { return referredUserId.get(); }
    public void setReferredUserId(String value) { referredUserId.set(value); }
    public StringProperty referredUserIdProperty() { return referredUserId; }

    // --- referralCode ---
    public String getReferralCode() { return referralCode.get(); }
    public void setReferralCode(String value) { referralCode.set(value); }
    public StringProperty referralCodeProperty() { return referralCode; }

    // --- timestamp ---
    public long getTimestamp() { return timestamp.get(); }
    public void setTimestamp(long value) { timestamp.set(value); }
    public LongProperty timestampProperty() { return timestamp; }
}
