package com.karru.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.karru.util.TextUtil.emailValidation;

/**
 * <h1>Validation</h1>
 * This class is used to validate the text fields
 * @author  3Embed
 * @since on 23-11-2017.
 */

public class Validation
{
    /**
     * <h2>emailValidation</h2>
     * <p>
     *     method to validate the input email format
     * </p>
     * @param email: input email
     * @return: flag: true if is valid
     */
    public static boolean validateEmailFormat(String email)
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * <h2>isValidMobile</h2>
     * used t ocheck if string is valid mobile
     * @param phone phone number
     * @return returns the true if valid mobile
     */
    public static boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    /**
     * <h2>updateAppVersion</h2>
     * <p>
     *     method to compare current app version with the retrieved version in app config
     * </p>
     * @return boolean : true if appversion retrieved from config api is greater than the installed version
     */
    public static boolean updateAppVersion(String currAppVersion, String configAppVersion)
    {
        return currAppVersion != null && !currAppVersion.isEmpty() && currAppVersion.compareToIgnoreCase(configAppVersion) < 0;
    }

    /**
     * <h2>validatePhoneEmail</h2>
     * This methos is used to validate the string is email od phone
     * @param phone_Mail string to be validated
     * @return returns the integer according to validation
     */
    public static int validatePhoneEmail(String phone_Mail)
    {
        if (phone_Mail.equals(""))
        {
            return 1;
        }
        else if (emailValidation(phone_Mail))
        {
            return 2;
        }
        else if (phone_Mail.matches("[0-9]+"))
        {
            if (phone_Mail.length() > 6)
                return 3;
            else
                return 4;
        }
        else
            return 5;
    }

}
