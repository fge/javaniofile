package com.github.fge.unicode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;

public final class Part1Decode
{
    private static final byte[] DATA = new byte[512];

    static {
        new Random().nextBytes(DATA);
    }

    public static void main(final String... args)
        throws IOException
    {
        System.out.println(new String(DATA));

        new Scanner(System.in).useDelimiter("").next();

        //final CodingErrorAction action = CodingErrorAction.REPLACE;
        //final CodingErrorAction action = CodingErrorAction.IGNORE;
        final CodingErrorAction action = CodingErrorAction.REPORT;

        //final Charset charset = Charset.defaultCharset();
        final Charset charset = StandardCharsets.UTF_8;

        final CharsetDecoder decoder = charset.newDecoder()
            .onMalformedInput(action);

        //System.out.println(decoder.decode(ByteBuffer.wrap(DATA)));

        final InputStream in = new ByteArrayInputStream(DATA);

        final Reader reader = new InputStreamReader(in, decoder);

        int c;
        while ((c = reader.read()) != -1)
            /* nothing */;
    }
}
