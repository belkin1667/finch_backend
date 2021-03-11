package com.belkin.finch_backend.util;

import com.belkin.finch_backend.exception.invalid.InvalidBase62Exception;
import lombok.Getter;

import java.util.Random;

public class Base62 {

    private static final int STRING_LENGTH = 11;
    private static final char[] ALPHABET = getAlphabet();
    private static char[] getAlphabet() {
        char[] alphabet = new char[62];
        int index = 0;
        for (char i = 'a'; i <= 'z'; i++) {
            alphabet[index] = i;
            index++;
        }
        for (char i = 'A'; i <= 'Z'; i++) {
            alphabet[index] = i;
            index++;
        }
        for (char i = '0'; i <= '9'; i++) {
            alphabet[index] = i;
            index++;
        }
        return alphabet;
    }

    public static Base62 randomBase62() {
        return randomBase62(STRING_LENGTH);
    }

    public static Base62 randomBase62(int length) {
        Random rnd = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = rnd.nextInt(62);
            builder.append(ALPHABET[index]);
        }
        return new Base62(builder.toString());
    }

    @Getter
    private String id;

    public void setId(String str) {
        if (isValidBase62(str)) {
            id = str;
        }
        else {
            throw new InvalidBase62Exception(str);
        }
    }

    public Base62(String str) {
        setId(str);
    }

    private static boolean isValidBase62(String str) {
        if (str == null || str.length() == 0)
            return false;

        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (!('0' <= c && c <= '9' || 'a' <= c && c <= 'z' || 'A' <= c && c <= 'Z')) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Base62) {
            Base62 other = (Base62) obj;
            return id.equals(other.toString());
        }
        else return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}