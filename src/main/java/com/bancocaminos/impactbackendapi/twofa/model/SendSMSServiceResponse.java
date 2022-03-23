package com.bancocaminos.impactbackendapi.twofa.model;

public class SendSMSServiceResponse {

    private boolean smsSent;
    private boolean twoFADeactivated;
    private boolean invalidPassword;

    public boolean isSmsSent() {
        return this.smsSent;
    }

    public boolean getSmsSent() {
        return this.smsSent;
    }

    public void setSmsSent(boolean smsSent) {
        this.smsSent = smsSent;
    }

    public boolean isTwoFADeactivated() {
        return this.twoFADeactivated;
    }

    public boolean getTwoFADeactivated() {
        return this.twoFADeactivated;
    }

    public void setTwoFADeactivated(boolean twoFADeactivated) {
        this.twoFADeactivated = twoFADeactivated;
    }

    public boolean isInvalidPassword() {
        return this.invalidPassword;
    }

    public boolean getInvalidPassword() {
        return this.invalidPassword;
    }

    public void setInvalidPassword(boolean invalidPassword) {
        this.invalidPassword = invalidPassword;
    }

}
