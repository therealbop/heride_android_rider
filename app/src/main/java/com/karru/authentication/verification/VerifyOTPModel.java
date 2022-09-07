package com.karru.authentication.verification;

import com.karru.dagger.ActivityScoped;

import javax.inject.Inject;

/**
 * <h2>VerifyOTPModel</h2>
 * This class is used to hole the data from Verification activity
 * @author 3Embed
 * @since on 04-12-2017.
 */
@ActivityScoped
public class VerifyOTPModel
{
    private String comingFrom ="" ;
    private String countryCode ="";
    private String mobileNumber;
    private String password,otpTime;

    @Inject
    VerifyOTPModel() {
    }

    public String getOtpTime() {
        return otpTime;
    }

    public void setOtpTime(String otpTime) {
        this.otpTime = otpTime;
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    String getCountryCode() {return countryCode;}

    void setCountryCode(String countryCode) {this.countryCode = countryCode;}

    String getMobileNumber() {return mobileNumber;}

    void setMobileNumber(String mobileNumber) {this.mobileNumber = mobileNumber;}

    public String getComingFrom() {return comingFrom;}

    public void setComingFrom(String comingFrom) {this.comingFrom = comingFrom;}
}
