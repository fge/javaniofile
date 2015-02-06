package com.github.fge.javaniofile;

import com.github.fge.filesystem.MorePaths;
import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Part1Paths
{
    public static void main(final String... args)
    {
        Path path = Paths.get("");

        // Equivalent to:
        // final Path path = FileSystems.getDefault().getPath("")

        System.out.println(path);
        System.out.println(path.toAbsolutePath());

        path = path.toAbsolutePath();

        // Can throw an IOException!
        // System.out.println(path.toRealPath());
        // System.out.println(path.toRealPath(LinkOption.NOFOLLOW_LINKS));

        // NO normalization is performed by default...
        System.out.println(path.resolve("../meh"));

        // You must do it yourself
        System.out.println(path.resolve("../meh").normalize());

        // Resolve to a path in the same "directory"
        System.out.println(path.resolveSibling("foo"));

        // Others: .getRoot(), .isAbsolute(), .getNameCount()

        Paths.get("c:foo"); // .getRoot() -> c:; isAbsolute() -> FALSE

        // ProviderMismatchException
        try (
            final FileSystem memoryfs = MemoryFileSystemBuilder.newLinux()
                .build("foo");
        ) {
            final Path other = memoryfs.getPath("xxx");
            //System.out.println(path.resolve(other));
            System.out.println(MorePaths.resolve(path, other));
        } catch (IOException wtf) {
            System.out.println("Meh, I didn't expect that...");
            wtf.printStackTrace(System.out);
        }
    }
}
