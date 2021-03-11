package com.belkin.finch_backend.api.devapi;

import java.util.Random;

public class StringGenerator extends Random {

    StringGenerator() {
        super();
    }

    StringGenerator(long seed) {
        super(seed);
    }


    String randomLatinString(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            Character c = (char)('a' + this.nextInt(26));
            builder.append(c);
        }
        return builder.toString();
    }

    String randomNumbersString(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            Character c = (char)('0' + this.nextInt(10));
            builder.append(c);
        }
        return builder.toString();
    }
}
