public class Utils {

    public static boolean isNumeric(String value) {
        if (value.length() == 0 ||
                (value.charAt(0) != '-' && Character.digit(value.charAt(0), 16) == -1)) {
            return false;
        }
        if (value.length() == 1 && value.charAt(0) == '-') {
            return false;
        }

        for (int i = 1; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (i == 1 && ch == '.') {
                continue;
            }
            if (Character.digit(ch, 16) == -1) {
                return false;
            }
        }
        return true;
    }
}
