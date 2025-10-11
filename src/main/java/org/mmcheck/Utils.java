package org.mmcheck;

import java.io.File;
import java.net.URISyntaxException;

import static org.mmcheck.Constants.DEFAULT_LIST_FILENAME;

public class Utils {
    /// DIRECTORY HANDLING
    private static File getAppExecutableFile() {
        try {
            return new File(
                    Utils.class
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()
            );
        } catch (URISyntaxException e) {
            e.printStackTrace();

            return new File(System.getProperty("user.dir"));
        }
    }

    public static String getAppParentDir() {
        return getAppExecutableFile().getParentFile().getAbsolutePath();
    }

    public static boolean checkFileInDir() {
        File modListFile = new File(
                Utils.getAppParentDir(),
                DEFAULT_LIST_FILENAME
        );

        return modListFile.exists() && modListFile.isFile();
    }

    /// USER INPUT HANDLING
    public static String userConsoleIn(String consoleIn) {
        return IO.readln(consoleIn).trim();
    }
}
