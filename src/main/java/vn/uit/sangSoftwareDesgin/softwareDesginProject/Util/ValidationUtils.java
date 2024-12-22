package vn.uit.sangSoftwareDesgin.softwareDesginProject.Util;

import java.util.regex.Pattern;

public class ValidationUtils {

    // Regex patterns for various validations
    private static final String PASSWORD_PATTERN =
            "^(?!.*['\";])(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,32}$";
    private static final String NAME_PATTERN =
            "^(?!.*['\";])[a-zA-Z0-9_.@-]{3,20}$";
    private static final String INPUT_PATTERN =
            "^(?!.*['\";])[\\w\\s]{8,32}$";
    private static final String EMAIL_PATTERN =
            "^(?!.*['\";])[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";



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
     * @return true if valid, false otherwise.
     */
    public static boolean isValidInput(String input) {
        return Pattern.matches(INPUT_PATTERN, input);
    }

    /**
     * Generic method to validate an input string with a custom regex pattern.
     *
     * @param email The email to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_PATTERN, email);
    }
}
