package com.github.fge.javaniofile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public final class Part1Paths
{
    public static void main(final String... args)
    {
        final Scanner scanner = new Scanner(System.in).useDelimiter("");

        /*
         * java.nio.file, aka JSR 203 (Java Specification Request)
         *
         * was: java.io.File, FileInputStream, FileReader, etc
         *
         * A Path is issued from a FileSystem.
         *
         * The JVM provides a default FileSystem. Its scheme is "file". Each
         * Path is uniquely identified by a URI (see RFC 3986).
         *
         * file:/foo/bar
         *
         * A FileSystem is issued from a FileSystemProvider.
         *
         * You can have more than one FileSystem.
         */

        final Path x = Paths.get("");

        // Equivalent to:
        // Path path = FileSystems.getDefault().getPath("")

        System.out.println("-->" + x + "<--");

        scanner.next();

        final Path path = x.toAbsolutePath();

        System.out.println(path);

        scanner.next();

        System.out.println(x.getRoot());
        System.out.println(path.getRoot());

        // x.equals(path) --> FALSE

        scanner.next();

        System.out.println(x.isAbsolute());
        System.out.println(path.isAbsolute());

        scanner.next();

        // final Path path = Paths.get("c:foo");
        // Windows: c:foo: getRoot() -> c: but isAbsolute() -> FALSE

        // NO normalization is performed by default...
        System.out.println(path.resolve("../meh"));

        scanner.next();

        // You must do it yourself
        System.out.println(path.resolve("../meh").normalize());

        scanner.next();

        // Resolve to a path in the same "directory"
        System.out.println(path.resolveSibling("foo"));

        scanner.next();

        System.out.println("The end");
    }
}
