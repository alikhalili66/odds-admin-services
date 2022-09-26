package ir.khalili.products.odds.core.helper;

import java.util.Random;

public class HelperPassword {

    public static String generatePassword() {
        StringBuilder stringBuilder = new StringBuilder();
        String[] chars = new String[]{"a", "A", "1", "b", "B", "2", "c", "C", "3", "d", "D", "4", "e", "E", "5", "f", "F", "6", "g", "G", "7", "h", "H", "8", "i", "I", "9", "j", "J", "0", "k", "K", "!", "l", "L", "@", "m", "M", "#", "n", "N", "$", "o", "O", "%", "p", "P", "^", "q", "Q", "&", "r", "R", "*", "s", "S", "(", "t", "T", ")", "u", "U", "v", "V", "+", "w", "W", "x", "X", "y", "Y", "z", "Z"};
        Random r = new Random();
        for (int i = 0; i <= 7; i++) {
            int randChar = Math.abs(r.nextInt()) % (chars.length - 1);
            stringBuilder.append(chars[randChar]);
        }
        return stringBuilder.toString();
    }
}
