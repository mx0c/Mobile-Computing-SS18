package ss18.mc.positime.utils;

import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

public class Validation {
    public static final String TAG = "Validation";

    /**
     * Function takes in 2 Strings and checks if they are equal.
     *
     * @param field1 First string to be compared
     * @param field2 Second string to be compared
     * @return returns true if both strings are equal
     */
    public static boolean validateFields(String field1, String field2) {
        if (field1.equals(field2)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Function checks if all TextInputLayout fields in an activity are filled.
     *
     * @param fields Array with TextInputLayout Fields
     * @return true, if all are filled
     */
    public static boolean checkForEmptyFields(TextInputLayout[] fields) {
        for (int i = 0; i < fields.length; i++) {
            TextInputLayout currentField = fields[i];

            if (currentField.getEditText().getText().toString().length() <= 0) {
                return false;
            }
        }
        return true;
    }


    /**
     * Function validates an email for correct format
     *
     * @param string Containing Email
     * @return true, if Email format is correct
     */
    public static boolean validateEmail(String string) {
        Log.d(TAG, "Validating email input: " + string);

        if (TextUtils.isEmpty(string) || !Patterns.EMAIL_ADDRESS.matcher(string).matches()) {

            return false;

        } else {

            return true;
        }
    }


    /**
     * Function validated an email for correct format
     *
     * @param field TextInputLayout containing mail
     * @return true, if Email format is correct
     */
    public static boolean validateEmail(TextInputLayout field) {
        String strField = field.getEditText().getText().toString();
        Log.d(TAG, "Validating email input: " + strField);

        if (TextUtils.isEmpty(strField) || !Patterns.EMAIL_ADDRESS.matcher(strField).matches()) {

            return false;

        } else {

            return true;
        }
    }
}
