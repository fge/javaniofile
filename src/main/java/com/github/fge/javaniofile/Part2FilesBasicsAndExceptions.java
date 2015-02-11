package com.github.fge.javaniofile;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

        // Files.exists()
        path1 = baseDir.resolve("dangling");
        path2 = baseDir.resolve("txtfile");

        System.out.println(Files.exists(path1)); // false
        System.out.println(Files.exists(path2)); // true

        scanner.next();

        // TRUE!
        System.out.println(Files.exists(path1, LinkOption.NOFOLLOW_LINKS));

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

        path1 = baseDir.resolve("noperms");
        path2 = baseDir.resolve("txtfile");

        // AccessDeniedException
        try {
            Files.copy(path1, path2, StandardCopyOption.REPLACE_EXISTING);
        } catch (FileSystemException e) {
            System.out.println(e.getClass().getSimpleName());
        } catch (IOException wtf) {
            System.out.println("F*ck");
            wtf.printStackTrace(System.out);
        }

        // Files.move()

        scanner.next();

        try {
            for (final Path entry: Files.newDirectoryStream(baseDir))
                System.out.println(baseDir.relativize(entry));
        } catch (IOException wtf) {
            System.out.println("F*ck");
            wtf.printStackTrace(System.out);
        }

        // With a PathFilter

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
    }
}
