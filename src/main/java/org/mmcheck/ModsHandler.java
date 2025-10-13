import java.io.IOException;
import java.nio.file.*;

public ModsHandler {
    public boolean checkContent(Path path) {
        Path directory = Paths.get(path.toString());

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            int index = 0;
            for (Path path : stream) {
                System.out.println(index + ": " + path.getFileName());
                index++;
            }

            IO.println(index);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
