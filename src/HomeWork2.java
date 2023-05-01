
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;



public class HomeWork2 {
    public static class LoginValidationException extends Exception {
        public LoginValidationException(String message) {
            super(message);
        }
    }


    public static void validateLogin(String login) throws LoginValidationException {
        if (login.length() > 20)
            throw new LoginValidationException("Login is too long");

        if (!login.matches("^[a-zA-Z0-9_]+$"))
            throw new LoginValidationException("Login contains illegal characters");

        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasDigit = false;
        boolean hasUnderscore = false;

        for (char c : login.toCharArray()) {
            if (Character.isLowerCase(c))
                hasLowerCase = true;
            else if (Character.isUpperCase(c))
                hasUpperCase = true;
            else if (Character.isDigit(c))
                hasDigit = true;
            else if (c == '_')
                hasUnderscore = true;
        }

        if (!(hasLowerCase && hasUpperCase && hasDigit && hasUnderscore))
            throw new LoginValidationException("Login does not meet complexity requirements");
    }

    public static Boolean isLoginValid(String login) {
        try {
            validateLogin(login);
            return true;
        } catch (LoginValidationException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("\nTests for validateLogin");
        for (int i = 0; i < loginList.size(); i++) {
            try {
                validateLogin(loginList.get(i));
                printTestCase(i, checkLoginResults.get(i).booleanValue(), true, 20);
            } catch (LoginValidationException e) {
                printTestCase(i, checkLoginResults.get(i).booleanValue(), false, 20);
            }
        }

        System.out.println("\nTests for isLoginValid");
        for (int i = 0; i < loginList.size(); i++) {
            printTestCase(i + loginList.size(),
                    checkLoginResults.get(i),
                    isLoginValid(loginList.get(i)),
                    20);
        }
    }

    private static void printTestCase(int num, boolean expected, boolean actual, int padding) {
        String res = (expected == actual) ? "PASSED" : "FAILED";
        String paddingStr = " ".repeat(padding - String.valueOf(num).length());
        String expectedStr = expected ? "✅" : "❌";
        String actualStr = actual ? "✅" : "❌";
        System.out.printf("%d.%s%sExpected: %s, actual: %s\n", num, paddingStr, res, expectedStr, actualStr);
    }

    /* Техническая секция - сюда писать ничего не надо */

    public static List<Boolean> checkLoginResults = Arrays.asList(
            true, true, true, false, false, false, true, false, false, false, false, true
    );

    public static List<String> loginList = Arrays.asList(
            "Minecraft_12",                                     // true
            "Player_3433",                                      // true
            "Dok_a111",                                         // true
            "Java",                                             // false
            "1122233",                                          // false
            "Play__",                                           // false
            "_Sun2_",                                           // true
            "____",                                             // false
            "Winx!",                                            // false
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa12_",            // false
            "WOWOWOOWOWOWOOWOWOWOWOWOW",                        // false
            "Correct_22"                                        // true
    );

    public static class AntiCheat {
        public static void run() {
            StringBuilder sb = new StringBuilder("");
            List<String> antiCheatList = new ArrayList<>();
            antiCheatList.addAll(loginList);
            antiCheatList.addAll(checkLoginResults.stream().map(Object::toString).toList());
            antiCheatList.add(sb.toString());
            calcHash(antiCheatList);
        };

        public static String bytesToHex(byte[] bytes) {
            char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
            char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; j++) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = HEX_ARRAY[v >>> 4];
                hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
            }
            return new String(hexChars);
        }

        public static void calcHash(List<String> list) {
            String total = String.join("", list);
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(total.getBytes());
                byte[] digest = md.digest();
                System.out.println("AntiCheatCheck: " + bytesToHex(digest));
            } catch (NoSuchAlgorithmException ignored) {}
        }
    }

    public static String constLen(String str, int len) {
        StringBuilder sb = new StringBuilder(str);
        while (len-- - str.length() > 0) sb.append(" ");
        return sb.toString();
    }

    public static void printTestCase(int n, Boolean exp, Boolean act, int minLen) {
        Function<String, String> green = str -> "\u001B[34m" + str + "\u001B[0m";
        Function<String, String> yellow = str -> "\u001B[33m" + str + "\u001B[0m";
        System.out.print( "TEST CASE " + constLen(String.valueOf(n), 4));
        System.out.print( "Ожидание: " + yellow.apply(constLen(exp.toString(), minLen)) + " Реальность: " + green.apply(constLen(act.toString(), minLen) + " "));
        if (Objects.equals(exp, act)) System.out.print("✅"); else System.out.print("❌");
        System.out.println();
    }

}