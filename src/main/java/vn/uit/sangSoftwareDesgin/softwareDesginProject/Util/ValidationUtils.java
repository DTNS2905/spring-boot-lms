package vn.uit.sangSoftwareDesgin.softwareDesginProject.Util;

import java.util.regex.Pattern;

public class ValidationUtils {

    // Regex patterns for various validations
    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    private static final String NAME_PATTERN =
            "^[a-zA-Z ]{2,50}$"; // Allow letters and spaces, between 2 and 50 characters

    /**
     * Validates if the given password meets the complexity requirements.
     *
     * @param password The password to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD_PATTERN, password);
    }

    /**
     * Validates if the given name is valid.
     *
     * @param name The name to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidName(String name) {
        return Pattern.matches(NAME_PATTERN, name);
    }

    /**
     * Generic method to validate an input string with a custom regex pattern.
     *
     * @param input   The input string to validate.
     * @param pattern The regex pattern to validate against.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidInput(String input, String pattern) {
        return Pattern.matches(pattern, input);
    }
}
