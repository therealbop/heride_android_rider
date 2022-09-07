package com.karru;

/**
 * <h1>ApplicationVersion</h1>
 * used to store the app versions
 */
public class ApplicationVersion
{
    /*"appVersion":"1.4",
"isMandatoryUpdateEnable":false,*/
    private String currenctAppVersion = "0";
    private boolean isMandatoryUpdateEnable;

    public String getCurrenctAppVersion() {
        return currenctAppVersion ;
    }

    public void setCurrenctAppVersion(String currenctAppVersion) {
        this.currenctAppVersion = currenctAppVersion;
    }

    public boolean isMandatoryUpdateEnable() {
        return isMandatoryUpdateEnable;
    }

    public void setMandatoryUpdateEnable(boolean mandatoryUpdateEnable) {
        isMandatoryUpdateEnable = mandatoryUpdateEnable;
    }
}
