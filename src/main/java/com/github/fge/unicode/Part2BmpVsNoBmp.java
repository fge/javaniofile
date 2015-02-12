package com.github.fge.unicode;

import java.util.Scanner;
import java.util.stream.IntStream;

public final class Part2BmpVsNoBmp
{
    private static final int PILE_OF_POO = 0x1f4a9;

    public static void main(final String... args)
    {

        // U+0000 -> U+FFFF: BMP (Basic Multilingual Plane)
        // U+10000 -> U+10FFFF: supplementary

        final String s = new StringBuilder().appendCodePoint(PILE_OF_POO)
            .toString();

        s.codePoints().forEach(i -> System.out.println(Integer.toHexString(i)));

        System.out.println(s.length());
        System.out.println(s.codePointCount(0, s.length()));

        new Scanner(System.in).useDelimiter("").next();

        final long definedCodePoints = IntStream.rangeClosed(0, 0x10ffff)
            .mapToObj(Character.UnicodeBlock::of)
            .filter(block -> block != null)
            .count();

        System.out.println(definedCodePoints);
        System.out.println(0x10ffff);

        System.out.println(100.0 * definedCodePoints / 0x10ffffL);

        final char[] chars = Character.toChars(PILE_OF_POO);

        for (final char c: chars)
            System.out.println(Integer.toHexString((int) c));
    }
}
