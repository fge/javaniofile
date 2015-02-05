package com.github.fge.javaniofile;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class Part2FilesBasicsAndExceptions
{
    private static final Consumer<Object> PRINT = System.out::println;

    public static void main(final String... args)
    {
        final Path baseDir = Paths.get("testdir");

        Path path1;
        Path path2;

        // Files.exists()
        path1 = baseDir.resolve("l1");
        path2 = baseDir.resolve("txtfile");

        System.out.println(Files.exists(path1)); // false
        System.out.println(Files.exists(path2)); // true

        // TRUE!
        System.out.println(Files.exists(path1, LinkOption.NOFOLLOW_LINKS));

        // Files.copy()

        path1 = baseDir.resolve("txtfile");
        path2 = baseDir.resolve("donttouch");

        // FileAlreadyExistsException
        try {
            Files.copy(path1, path2);
        } catch (FileSystemException e) {
            System.out.println(e.getClass().getSimpleName());
        } catch (IOException wtf) {
            System.out.println("Meh, I didn't expect that...");
            wtf.printStackTrace(System.out);
        }

        path1 = baseDir.resolve("noperms");
        path2 = baseDir.resolve("txtfile");

        // AccessDeniedException
        try {
            Files.copy(path1, path2, StandardCopyOption.REPLACE_EXISTING);
        } catch (FileSystemException e) {
            System.out.println(e.getClass().getSimpleName());
        } catch (IOException wtf) {
            System.out.println("Meh, I didn't expect that...");
            wtf.printStackTrace(System.out);
        }

        // Files.move()

        // Files.newDirectoryStream()

        try {
            for (final Path entry: Files.newDirectoryStream(baseDir))
                System.out.println(baseDir.relativize(entry));
        } catch (IOException wtf) {
            System.out.println("Meh, I didn't expect that...");
            wtf.printStackTrace(System.out);
        }

        // With a PathFilter

        // Files.list(), Files.find(), Files.walk() (Java 8)

        try (
            final Stream<Path> entries = Files.list(baseDir);
        ) {
            entries.forEach(PRINT);
        } catch (IOException wtf) {
            System.out.println("Meh, I didn't expect that...");
            wtf.printStackTrace(System.out);
        }
    }
}
