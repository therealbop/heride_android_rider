package com.karru.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.karru.utility.Utility;
import com.heride.rider.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h1>TextUtil</h1>
 * This class is used to check for texts related checks
 *@author  3Embed
 * @since on 23-11-2017.
 */

public class TextUtil
{
    /**
     * <h2>isEmpty</h2>
     * This method is used to check whether the text is empty
     * @param string The string to be validated
     * @return returns true if its empty else false
     */
    public static boolean isEmpty(String string) {
        return string == null || string.replace(" ", "").length() == 0;
    }

    /**
     * <h2>emailValidation</h2>
     * <p>
     *     method to validate the input email format
     * </p>
     * @param email: input email
     * @return: flag: true if is valid
     */
    public static boolean emailValidation(String email)
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * <h2>phoneNumberLengthValidation</h2>
     * This method is used to validate the length of the phone number
     * @param countryCode country code
     * @return returns true if valid else false
     */
    public static boolean phoneNumberLengthValidation(String mobileNumber, String countryCode)
    {
        String phoneNumberE164Format = countryCode.concat(mobileNumber);
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumberProto = phoneUtil.parse(phoneNumberE164Format, null);
            boolean isValid = phoneUtil.isValidNumber(phoneNumberProto); // returns true if valid
            Utility.printLog("phoneNumberLengthValidation"+" is valid "+isValid);
            return isValid;
        }catch (NumberParseException e){
            Utility.printLog("phoneNumberLengthValidation"+" NumberParseException "+e);
        }
        return false;
    }
}
