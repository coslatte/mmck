package org.mmck;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.random.RandomGenerator;

public class App {
    static void main() {
        String code = String.join(
                "-",
                Integer.toHexString(RandomGenerator.getDefault().nextInt()),
                "[" + OffsetDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm")) + "]"
        );
        System.out.println(code);
    }
}
