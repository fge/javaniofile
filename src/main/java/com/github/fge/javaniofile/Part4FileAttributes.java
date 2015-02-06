package com.github.fge.javaniofile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.util.Random;
import java.util.function.Consumer;

public final class Part4FileAttributes
{
    private static final Consumer<Object> PRINT = System.out::println;

    private static final int SIZE = 20298;

    public static void main(final String... args)
        throws IOException
    {
        final Path basedir = Paths.get("testdir");

        final byte[] content = new byte[SIZE];
        final Random random = new Random();

        random.nextBytes(content);

        final Path path = basedir.resolve("victim");

        try (
            final OutputStream out = Files.newOutputStream(path);
        ) {
            out.write(content);
        }

        // Size

        System.out.println(Files.size(path));

        System.out.println(Files.getAttribute(path, "basic:size"));

        final BasicFileAttributeView basicView
            = Files.getFileAttributeView(path, BasicFileAttributeView.class);
        System.out.println(basicView.readAttributes().size());


        // Permissions

        System.out.println(Files.getPosixFilePermissions(path));

        System.out.println(Files.getAttribute(path, "posix:permissions"));

        final PosixFileAttributeView posixView
            = Files.getFileAttributeView(path, PosixFileAttributeView.class);
        System.out.println(posixView.readAttributes().permissions());

        // Last modified time

        System.out.println(Files.getLastModifiedTime(path));

        System.out.println(Files.getAttribute(path, "basic:lastModifiedTime"));

        final BasicFileAttributeView basicView2
            = Files.getFileAttributeView(path, BasicFileAttributeView.class);
        System.out.println(basicView2.readAttributes().lastModifiedTime());

        // Supported views

        FileSystems.getDefault().supportedFileAttributeViews()
            .forEach(PRINT);
    }
}
