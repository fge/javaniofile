package com.github.fge.javaniofile;

import com.github.fge.filesystem.provider.FileSystemRepository;
import com.github.fge.fs.dropbox.provider.DropBoxFileSystemProvider;
import com.github.fge.fs.dropbox.provider.DropBoxFileSystemRepository;
import com.github.fge.ftpfs.FtpFileSystemProvider;
import com.github.fge.ftpfs.io.commonsnetimpl.CommonsNetFtpAgentFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class Part5FileSystems
{
    private static final Path DROPBOX_SECRETS;

    static {
        final Path path = Paths.get(System.getProperty("user.home"),
            ".dropbox/appsecret.properties");
        try {
            DROPBOX_SECRETS = path.toRealPath();
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static final String ZIP_PATH
        = "/home/fge/src/perso/grappa-debugger/tracefiles/json.zip";
    private static final String INFO_CSV = "info.csv";

    public static void main(final String... args)
        throws IOException
    {
        final Scanner scanner = new Scanner(System.in).useDelimiter("");

        final Path dstdir = Paths.get("testdir");

        final Path link = dstdir.resolve("link");
        final Path symlink = dstdir.resolve("symlink");

        // Cleanup...

        Files.deleteIfExists(link);
        Files.deleteIfExists(symlink);

        // Files.isSameFile()

        final Path orig = dstdir.resolve("txtfile");

        Files.createLink(link, orig);

        System.out.println(Files.isSameFile(link, orig));

        scanner.next();

        Files.createSymbolicLink(symlink, dstdir.relativize(orig));

        System.out.println(Files.isSameFile(symlink, orig));

        scanner.next();

        // Create a zip filesystem

        final Path zip = Paths.get(ZIP_PATH);

        final Map<String, ?> env = Collections.singletonMap("readonly", "true");

        final URI uri = URI.create("jar:" + zip.toUri());

        try (
            final FileSystem zipfs = FileSystems.newFileSystem(uri, env);
        ) {
            Files.copy(zipfs.getPath(INFO_CSV), dstdir.resolve(INFO_CSV),
                StandardCopyOption.REPLACE_EXISTING);
        }

        System.out.println("Done");
        
        scanner.next();

        try (
            final FileSystem srcfs = createSourceFileSystem();
            final FileSystem dstfs = createDestinationFileSystem();
        ) {
            final Path srcPath = srcfs.getPath("/pub/linux/lg/README");
            final Path dstPath = dstfs.getPath("/testReadme");
            Files.copy(srcPath, dstPath);
        }
    }

    private static FileSystem createSourceFileSystem()
        throws IOException
    {
        /*
         * Source filesystem
         */
        final FtpFileSystemProvider provider
            = new FtpFileSystemProvider(new CommonsNetFtpAgentFactory());
        final Map<String, String> env = Collections.emptyMap();
        final URI uri = URI.create("ftp://ftp.lip6.fr");

        return provider.newFileSystem(uri, env);
    }

    private static FileSystem createDestinationFileSystem()
        throws IOException
    {
        final URI uri = URI.create("dropbox://foo");
        final Properties properties = new Properties();
        try (
            final BufferedReader reader
                = Files.newBufferedReader(DROPBOX_SECRETS, UTF_8);
        ) {
            properties.load(reader);
        }

        final Map<String, String> env = Collections.singletonMap(
            "accessToken", properties.getProperty("accessToken")
        );

        final FileSystemRepository repository
            = new DropBoxFileSystemRepository();
        final FileSystemProvider provider
            = new DropBoxFileSystemProvider(repository);

        return provider.newFileSystem(uri, env);
    }
}
