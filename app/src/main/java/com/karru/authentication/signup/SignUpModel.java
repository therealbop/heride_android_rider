package com.karru.authentication.signup;


import com.karru.authentication.UserDetailsDataModel;

/**
 * <h1>SignUpModel</h1>
 * This class is created to hold the data of sign up
 * @author  3Embed
 * @since on 01-12-2017.
 */

class SignUpModel
{
    private boolean isAllPermissionsGranted;
    private boolean mBusinessAccount , isProfilePicSelected, isImageUploaded;
    private boolean isFullNameValid;
    private boolean isPhoneValid;
    private boolean isPasswordValid;
    private boolean isCompanyNameValid;
    private boolean isCompanyAddressValid;
    private boolean isReferralCodeEntered;
    private String socialMediaId,referralCode ;
    private int countryCodeMinLength , countryCodeMaxLength;
    private UserDetailsDataModel userDetailsDataModel;

    String getSocialMediaId() {
        return socialMediaId;
    }

    void setSocialMediaId(String socialMediaId) {
        this.socialMediaId = socialMediaId;
    }

    int getCountryCodeMinLength() {return countryCodeMinLength;}

    void setCountryCodeMinLength(int countryCodeMinLength) {this.countryCodeMinLength = countryCodeMinLength;}

    int getCountryCodeMaxLength() {
        return countryCodeMaxLength;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    void setCountryCodeMaxLength(int countryCodeMaxLength) {
        this.countryCodeMaxLength = countryCodeMaxLength;
    }

    boolean isReferralCodeEntered() {return isReferralCodeEntered;}

    void setReferralCodeEntered(boolean referralCodeEntered) {isReferralCodeEntered = referralCodeEntered;}

    boolean isCompanyAddressValid() {return isCompanyAddressValid;}

    void setCompanyAddressValid(boolean companyAddressValid) {isCompanyAddressValid = companyAddressValid;}

    boolean isCompanyNameValid() {return isCompanyNameValid;}

    void setCompanyNameValid(boolean companyNameValid) {isCompanyNameValid = companyNameValid;}

    boolean isEmailValid() {return isEmailValid;}

    boolean isPasswordValid() {return isPasswordValid;}

    void setPasswordValid(boolean passwordValid) {isPasswordValid = passwordValid;}

    void setEmailValid(boolean emailValid) {isEmailValid = emailValid;}

    private boolean isEmailValid;

    boolean isPhoneValid() {return isPhoneValid;}

    void setPhoneValid(boolean phoneValid) {isPhoneValid = phoneValid;}

    boolean isFullNameValid() {return isFullNameValid;}

    void setFullNameValid(boolean fullNameValid) {isFullNameValid = fullNameValid;}

    boolean isAllPermissionsGranted() {
        return isAllPermissionsGranted;
    }

    void setAllPermissionsGranted(boolean allPermissionsGranted) {isAllPermissionsGranted = allPermissionsGranted;}

    boolean isImageUploaded() {return isImageUploaded;}

    void setImageUploaded(boolean imageUploaded) {
        isImageUploaded = imageUploaded;
    }

    boolean isProfilePicSelected() {return isProfilePicSelected;}

    void setProfilePicSelected(boolean profilePicSelected) {isProfilePicSelected = profilePicSelected;}

    boolean isBusinessAccount() {
        return mBusinessAccount;
    }

    void setBusinessAccount(boolean mBusinessAccount) {
        this.mBusinessAccount = mBusinessAccount;
    }

    UserDetailsDataModel getUserDetailsDataModel() {
        return userDetailsDataModel;
    }

    void setUserDetailsDataModel(UserDetailsDataModel userDetailsDataModel) {
        this.userDetailsDataModel = userDetailsDataModel;
    }
}
