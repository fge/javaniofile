package com.github.fge.javaniofile;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;

public final class Part4FileAttributes
{
    private static final Consumer<Object> PRINT = System.out::println;

    private static final int SIZE = 20298;

    public static void main(final String... args)
        throws IOException
    {
        final Scanner scanner = new Scanner(System.in).useDelimiter("");

        /*
         * AclFileAttributeView         -> "acl"
         * BasicFileAttributeView       -> "basic"
         * DosFileAttributeView         -> "dos"
         * FileOwnerFileAttributeView   -> "owner"
         * PosixFileAttributeView       -> "posix"
         * UserDefinedFileAttributeView -> "user"
         */


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

        scanner.next();

        // Size

        System.out.println(Files.size(path));

        System.out.println(Files.getAttribute(path, "basic:size"));

        final BasicFileAttributeView basicView
            = Files.getFileAttributeView(path, BasicFileAttributeView.class);
        System.out.println(basicView.readAttributes().size());

        final BasicFileAttributes attrs
            = Files.readAttributes(path, BasicFileAttributes.class);

        System.out.println(attrs.size());

        scanner.next();

        // Last modified time

        System.out.println(Files.getLastModifiedTime(path));

        System.out.println(Files.getAttribute(path, "basic:lastModifiedTime"));

        System.out.println(basicView.readAttributes().lastModifiedTime());

        System.out.println(attrs.lastModifiedTime());

        scanner.next();


        // Posix permissions

        System.out.println(Files.getPosixFilePermissions(path));

        System.out.println(Files.getAttribute(path, "posix:permissions"));

        final PosixFileAttributeView posixView
            = Files.getFileAttributeView(path, PosixFileAttributeView.class);
        System.out.println(posixView.readAttributes().permissions());

        final PosixFileAttributes attrs2
            = Files.readAttributes(path, PosixFileAttributes.class);
        final Set<PosixFilePermission> permissions = attrs2.permissions();
        System.out.println(permissions);

        System.out.println(PosixFilePermissions.toString(permissions));

        scanner.next();

        // Supported views

        FileSystems.getDefault().supportedFileAttributeViews()
            .forEach(PRINT);

        scanner.next();

        Files.readAttributes(path, "unix:*").keySet()
            .forEach(PRINT);
    }
}
