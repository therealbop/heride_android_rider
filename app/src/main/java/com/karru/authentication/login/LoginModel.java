package com.karru.authentication.login;

import com.karru.util.Validation;

import javax.inject.Inject;

/**
 * <h1>LoginModel</h1>
 * This class is used to do some logical interpretation
 * @author  3Embed
 * @since 23-11-2017.
 */

public class LoginModel
{
    private boolean validEmail;
    private int loginType; // 1 facebook || 2 google

    @Inject
    LoginModel()
    {
    }

    int getLoginType()
    {
        return loginType;
    }

    void setLoginType(int loginType)
    {
        this.loginType = loginType;
    }

    /**
     * <h2>isValidEmail</h2>
     * This method is used to retrieve whether email is valid
     * @return  returns true if valid else returns false
     */
    boolean isValidEmail() {
        return validEmail;
    }
    /**
     * <h2>setValidEmail</h2>
     * This method is used to store whether email is valid
     */
    private void setValidEmail(boolean validEmail) {
        this.validEmail = validEmail;
    }

    /**
     * <h2>validateUserName</h2>
     * This method is used to verify given string is number or not
     * <p>
     *     If its number then validate phone number and if email then validate email
     * </p>
     * @param userName String given to verify
     * @return returns 1 for invalid number else if returns 2 for invalid email
     */
    int validateUserName(String userName) {
        if (userName.matches("[0-9-+]+"))
        {
            if(!(userName.length() > 6 && userName.length() < 17))
            {
                setValidEmail(false);
                return 1;
            }
            else
                setValidEmail(true);
        }
        else
        {
            if (!Validation.validateEmailFormat(userName))
            {
                setValidEmail(false);
                return 2;
            }
            else
                setValidEmail(true);
        }
        return 0;
    }
}
