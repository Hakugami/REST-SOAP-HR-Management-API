package utils;



import java.util.regex.Pattern;

public class ValidationUtil {

    // Regular expression for email validation
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    public static boolean isValidEmailFormat(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        return pattern.matcher(email).matches();
    }

    public static String validatePhoneNumber(String phoneNumber) {
        // Check the length of the phone number
        if (phoneNumber.length() != 11) {
            return "Phone number must be 11 digits long";
        }

        // Check if the phone number contains only digits
        if (!phoneNumber.matches("\\d+")) {
            return "Phone number must contain only digits";
        }


        // check if valid format
        if (!hasValidFormat(phoneNumber)) {
            return "Invalid phone number format";
        }

        // If all checks pass, return null
        return null;
    }

    private static boolean hasValidFormat(String phoneNumber) {
        String regex = "^01[0125][0-9]{8}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(phoneNumber).matches();
    }


    public static String validatePassword(String password) {
        // Check the length of the password
        if (password.length() < 8) {
            return "Password must be at least 8 characters long";
        }

        // Check for uppercase letters
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter";
        }

        // Check for lowercase letters
        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter";
        }

        // Check for numbers
        if (!password.matches(".*[0-9].*")) {
            return "Password must contain at least one number";
        }

        // Check for special characters
        if (!password.matches(".*[!@#$%^&*].*")) {
            return "Password must contain at least one special character (!@#$%^&*)";
        }

        // If all checks pass, return null
        return null;
    }
}