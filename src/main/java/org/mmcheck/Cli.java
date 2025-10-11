package org.mmcheck;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.mmcheck.Constants.DEFAULT_LIST_FILENAME;
import static org.mmcheck.Constants.isNo;
import static org.mmcheck.Constants.isYes;
import static org.mmcheck.Utils.getAppParentDir;
import static org.mmcheck.Utils.userConsoleIn;

public class Cli {
    public boolean init() {
        boolean jsonExists = Utils.checkFileInDir();

        if (jsonExists) {
            System.out.printf("OK > modlist file in directory (%s)%n.", DEFAULT_LIST_FILENAME);
        } else {
            Path folderPath = null;
            System.out.printf("NOT FOUND > The file %s is not in the current directory, want to create this file at some specific location? [Y, N]: ", DEFAULT_LIST_FILENAME);
            System.out.flush();

            boolean defaultPath = false;

            while (true) {
                String userInput = userConsoleIn("");

                if (isYes(userInput)) {
                    while (true) {
                        folderPath = Path.of(IO.readln("PATH > Specify the full path (parent directory): "));
                        Path parentDir = folderPath;

                        if (Files.exists(parentDir) && Files.isDirectory(parentDir)) {
                            System.out.println("/// Directory exists: " + parentDir);
                            break;
                        } else {
                            System.out.println("/// Directory does not exist or is invalid. Try again (or press 'D' for default): ");

                            if (userConsoleIn("").equalsIgnoreCase("d")) {
                                defaultPath = true;
                                break;
                            }
                        }
                    }
                } else if (isNo(userInput)) {
                    defaultPath = true;
                } else {
                    IO.println("Wrong input, try again: ");
                    IO.print("> ");
                    continue;
                }

                break;
            }

            if (defaultPath) folderPath = Path.of(getAppParentDir(), DEFAULT_LIST_FILENAME); // In case it's default...

            //
            // CREATING THE FILE
            //
            Path parentDir = folderPath.getParent();

            try {
                Files.createDirectories(parentDir);

                if (Files.notExists(folderPath)) {
                    Files.createFile(folderPath);
                    System.out.println("/// File created successfully: " + Path.of(String.valueOf(folderPath), DEFAULT_LIST_FILENAME));
                } else {
                    System.out.println("/// File already exists: " + folderPath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.print("/// Start checking the mods integrity? [Y, N]: ");
        System.out.flush();

        do {
            String userInput = userConsoleIn("").toLowerCase();

            if (userInput.equals("c")) System.exit(0);

            if (isYes(userInput)) {
                return true;
            } else if (isNo(userInput)) {
                return false;
            } else {
                IO.println("/// Please, introduce a valid option to continue. You can type 'C' to exit.");
                IO.print("> ");
            }
        } while (true);
    }

}
