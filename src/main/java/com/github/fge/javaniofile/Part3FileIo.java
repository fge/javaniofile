package com.github.fge.javaniofile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class Part3FileIo
{
    private static final Consumer<Object> PRINT = System.out::println;

    public static void main(final String... args)
    {
        // FileSystemProvider

        final Path baseDir = Paths.get("testdir");

        // newOutputStream
        final Random random = new Random();
        final byte[] content = new byte[1024];
        random.nextBytes(content);

        Path victim;

        victim = baseDir.resolve("foobar");

        try (
            final OutputStream out = Files.newOutputStream(victim);
        ) {
            out.write(content);
        } catch (IOException wtf) {
            System.out.println("Meh, I didn't expect that...");
            wtf.printStackTrace(System.out);
        }

        // Appending

        random.nextBytes(content);

        try (
            final OutputStream out = Files.newOutputStream(victim,
                StandardOpenOption.APPEND);
        ) {
            out.write(content);
        } catch (IOException wtf) {
            System.out.println("Meh, I didn't expect that...");
            wtf.printStackTrace(System.out);
        }

        // CREATE, CREATE_NEW, TRUNCATE_EXISTING
        try (
            final OutputStream out = Files.newOutputStream(victim,
                StandardOpenOption.CREATE_NEW);
        ) {
            out.write(content);
        } catch (FileSystemException e) {
            System.out.println(e.getClass().getSimpleName());
        } catch (IOException wtf) {
            System.out.println("Meh, I didn't expect that...");
            wtf.printStackTrace(System.out);
        }

        try {
            Files.delete(victim);
        } catch (IOException wtf) {
            System.out.println("Meh, I didn't expect that...");
            wtf.printStackTrace(System.out);
        }

        // Java 7

        victim = baseDir.resolve("txtfile");

        try (
            final BufferedReader reader = Files.newBufferedReader(victim,
                StandardCharsets.UTF_8);
        ) {
            String line;
            while ((line = reader.readLine()) != null)
                System.out.println(line);
        } catch (IOException wtf) {
            System.out.println("Meh, I didn't expect that...");
            wtf.printStackTrace(System.out);
        }

        // Java 8

        try (
            final Stream<String> lines = Files.lines(victim,
                StandardCharsets.UTF_8);
        ) {
            lines.forEach(PRINT);
        } catch (IOException wtf) {
            System.out.println("Meh, I didn't expect that...");
            wtf.printStackTrace(System.out);
        }
    }
}
