package com.github.fge.javaniofile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Map;

public final class Part5FileSystemsBasics
{
    private static final String ZIP_PATH
        = "/home/fge/src/perso/grappa-debugger/tracefiles/json.zip";
    private static final String INFO_CSV = "info.csv";

    public static void main(final String... args)
    {
        // Create a zip filesystem

        final Path zip = Paths.get(ZIP_PATH);

        final Map<String, ?> env = Collections.singletonMap("readonly", "true");

        final URI uri = URI.create("jar:" + zip.toUri());

        final Path dstdir = Paths.get("testdir");

        try (
            final FileSystem zipfs = FileSystems.newFileSystem(uri, env);
        ) {
            Files.copy(zipfs.getPath(INFO_CSV), dstdir.resolve(INFO_CSV),
                StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException wtf) {
            System.out.println("Meh, I didn't expect that...");
            wtf.printStackTrace(System.out);
        }

        /*
         * Other filesystem implementations:
         *
         * https://github.com/marschall/memoryfilesystem (memory)
         * https://github.com/fge/java7-fs-dropbox (dropbox)
         * https://github.com/fge/java7-fs-box (box)
         * https://github.com/fge/java7-fs-ftp (ftp)
         *
         * And others
         */
    }
}
