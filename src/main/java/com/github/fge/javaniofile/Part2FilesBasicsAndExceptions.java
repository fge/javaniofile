package com.github.fge.javaniofile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class Part2FilesBasicsAndExceptions
{
    private static final Consumer<Object> PRINT = System.out::println;

    public static void main(final String... args)
    {
        final Scanner scanner = new Scanner(System.in).useDelimiter("");

        final Path baseDir = Paths.get("testdir");

        Path path1;
        Path path2;

        // Files.exists() and symbolic links

        path1 = baseDir.resolve("dangling");
        path2 = baseDir.resolve("txtfile");

        System.out.println(Files.exists(path1)); // false
        System.out.println(Files.exists(path2)); // true

        scanner.next();

        // TRUE!
        System.out.println(Files.exists(path1, LinkOption.NOFOLLOW_LINKS));

        // stat(2) vs lstat(2)

        scanner.next();

        // Files.copy()

        path1 = baseDir.resolve("txtfile");
        path2 = baseDir.resolve("alreadyThere");

        // FileAlreadyExistsException
        try {
            Files.copy(path1, path2);
        } catch (FileSystemException e) {
            System.out.println(e.getClass().getSimpleName());
        } catch (IOException wtf) {
            System.out.println("F*ck");
            wtf.printStackTrace(System.out);
        }

        scanner.next();

        try {
            Files.copy(path1, path2, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException wtf) {
            System.out.println("F*ck");
            wtf.printStackTrace(System.out);
        }


        scanner.next();

        // Files.move()

        final byte[] data = new byte[1024];
        new Random().nextBytes(data);

        path1 = baseDir.resolve("foo");

        try (
            final OutputStream out = Files.newOutputStream(path1);
        ) {
            out.write(data);
        } catch (IOException wtf) {
            System.out.println("F*ck");
            wtf.printStackTrace(System.out);
        }

        scanner.next();

        try {
            for (final Path entry: Files.newDirectoryStream(baseDir))
                System.out.println(baseDir.relativize(entry));
        } catch (IOException wtf) {
            System.out.println("F*ck");
            wtf.printStackTrace(System.out);
        }

        scanner.next();

        // With a PathMatcher

        try {
            for (final Path entry: Files.newDirectoryStream(baseDir, "*txt*"))
                System.out.println(baseDir.relativize(entry));
        } catch (IOException wtf) {
            System.out.println("F*ck");
            wtf.printStackTrace(System.out);
        }

        // Files.list(), Files.find(), Files.walk() (Java 8)

        scanner.next();

        try (
            final Stream<Path> entries = Files.list(baseDir);
        ) {
            entries.forEach(PRINT);
        } catch (IOException wtf) {
            System.out.println("F*ck");
            wtf.printStackTrace(System.out);
        }

        scanner.next();

        System.out.println("The end");
    }
}
