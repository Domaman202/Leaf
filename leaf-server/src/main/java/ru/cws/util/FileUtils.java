package ru.cws.util;

import java.io.File;

public class FileUtils {
    public static int indexOfSeparator(String path) {
        return normalizePath(path).indexOf(File.separatorChar);
    }

    public static int indexOfSeparator(String path, int fromIndex) {
        return normalizePath(path).indexOf(File.separatorChar, fromIndex);
    }

    public static int lastIndexOfSeparator(String path) {
        return normalizePath(path).lastIndexOf(File.separatorChar);
    }

    public static int lastIndexOfSeparator(String path, int fromIndex) {
        return normalizePath(path).lastIndexOf(File.separatorChar, fromIndex);
    }

    public static String concatPath(String...paths) {
        if (paths.length == 0)
            return "";
        var path = new StringBuilder(paths[0]);
        for (int i = 1; i < paths.length; i++) {
            path.append(File.separatorChar).append(paths[i]);
        }
        return normalizePath(path.toString());
    }

    public static String normalizePath(String path) {
        return new File(path).getPath();
    }
}
