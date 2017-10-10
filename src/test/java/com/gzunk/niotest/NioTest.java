package com.gzunk.niotest;

import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class NioTest {

    private Path getPathFromResource(String resource) {
        Path source = null;
        try {
            source = Paths.get(NioTest.class.getClassLoader().getResource(resource).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return source;
    }

    @Test
    public void testFilesCopyToInputStream() throws IOException {

        InputStream in = new ByteArrayInputStream("ABCDEFGH".getBytes());
        Path target = Paths.get("test.txt");
        CopyOption[] options = new CopyOption[0];

        Files.copy(in, target, options);
        assertTrue(Files.exists(target));

        List<String> lines = Files.readAllLines(target);
        assertNotNull(lines);

        assertEquals("ABCDEFGH", lines.get(0));

        Files.deleteIfExists(target);
        assertFalse(Files.exists(target));

    }

    @Test
    public void testFilesCopyToOutputStream() throws IOException {

        Path source = getPathFromResource("test.txt");
        ByteArrayOutputStream out = new ByteArrayOutputStream(256);

        Files.copy(source, out);

        String data = new String(out.toByteArray());
        assertEquals("ABCDEFGH", data);

    }

    @Test
    public void testFilesCopyPathToPath() throws IOException {

        Path source = getPathFromResource("test.txt");
        Path target = Paths.get("test.txt");

        Files.copy(source, target);
        assertTrue(Files.exists(target));

        List<String> lines = Files.readAllLines(target);
        assertNotNull(lines);

        assertEquals("ABCDEFGH", lines.get(0));

        Files.deleteIfExists(target);
        assertFalse(Files.exists(target));

    }

    @Test
    public void testCreateDirectories() throws IOException {

        Path parent = Paths.get("parent");
        Path child = Paths.get("parent", "child");
        Path grandchild = Paths.get("parent", "child", "grandchild");

        Files.createDirectories(grandchild);

        assertTrue(Files.exists(parent));
        assertTrue(Files.exists(child));
        assertTrue(Files.exists(grandchild));

        assertTrue(Files.isDirectory(parent));
        assertTrue(Files.isDirectory(child));
        assertTrue(Files.isDirectory(grandchild));

        Files.delete(grandchild);
        Files.delete(child);
        Files.delete(parent);

        assertFalse(Files.exists(parent));
        assertFalse(Files.exists(child));
        assertFalse(Files.exists(grandchild));
    }

    @Test
    public void testCreateDirectory() throws IOException {

        Path parent = Paths.get("parent");
        Files.createDirectory(parent);
        assertTrue(Files.exists(parent));
        assertTrue(Files.isDirectory(parent));
        Files.delete(parent);
        assertFalse(Files.exists(parent));

    }

    @Test
    public void testMakeFileUnreadable() throws IOException {
        Path source = getPathFromResource("test.txt");
        Path target = Paths.get("test.txt");

        Set<PosixFilePermission> perms = new HashSet<>();
        perms.add(PosixFilePermission.OWNER_WRITE);

        Files.copy(source, target);

        assertTrue(Files.exists(target));
        assertTrue(Files.isReadable(target));

        // Files.setPosixFilePermissions(target, perms);
        // assertTrue(Files.exists(target));
        // assertFalse(Files.isReadable(target));

        Files.deleteIfExists(target);
        assertFalse(Files.exists(target));

    }
}
