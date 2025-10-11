package org.mmcheck;

import java.util.Set;

public class Constants {
    public static String DEFAULT_LIST_FILENAME = "modlist.json";
    private static final Set<String> yesSet = Set.of("yes", "y");
    private static final Set<String> noSet = Set.of("no", "n");

    public static boolean isYes(String s) {
        return yesSet.contains(s);
    }

    public static boolean isNo(String s) {
        return noSet.contains(s);
    }
}
