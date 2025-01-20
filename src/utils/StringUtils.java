package utils;

public class StringUtils {
    public static boolean isPalindrome(String string) {
        string = string.trim().toLowerCase();
        if(!string.isEmpty() && string.charAt(0) == string.charAt(string.length() - 1)) {
            if(string.length() == 1) {
                return true;
            } else {
                StringBuilder sb = new StringBuilder(string);

                sb.deleteCharAt(string.length() - 1);
                sb.deleteCharAt(0);

                return isPalindrome(sb.toString());
            }
        } else {
            return false;
        }
    }
}
