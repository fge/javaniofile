package com.github.fge.javaniofile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public final class Part1Paths
{
    public static void main(final String... args)
    {
        final Scanner scanner = new Scanner(System.in).useDelimiter("");

        Path path = Paths.get("");

        // Equivalent to:
        // final Path path = FileSystems.getDefault().getPath("")

        System.out.println(path);

        scanner.next();

        System.out.println(path.toAbsolutePath());

        scanner.next();

        path = path.toAbsolutePath();

        // Can throw an IOException!
        // System.out.println(path.toRealPath());
        // System.out.println(path.toRealPath(LinkOption.NOFOLLOW_LINKS));

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
