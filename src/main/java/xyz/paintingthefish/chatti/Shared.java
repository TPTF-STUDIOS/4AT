package xyz.paintingthefish.chatti;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.ini4j.Wini;

public class Shared {
    public static Wini get_ini_from_fpath(Path path) {
        File f = path.toFile();

        try (FileInputStream is = new FileInputStream(f)) {
            return new Wini(is);
        } catch (FileNotFoundException e) {
            System.out.println("[INFO] Config file not found at: " + path + ". Creating blank INI configuration.");
            Wini ini = new Wini();
            try {
                // 1. Get the parent folder path
                Path parentDir = path.getParent();

                // 2. Create the missing folders if they don't exist
                if (parentDir != null) {
                    Files.createDirectories(parentDir);
                }

                // 3. Create the empty file
                Files.createFile(path);

                // 4. Save the blank INI structure to the file
                ini.store(f);

                // FIX: Return the active ini object instead of an empty dummy
                return ini;

            } catch (IOException err) {
                System.out.println("[ERROR] Could not create empty config file: " + err.getMessage());
                return new Wini();
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Invalid file type or read failure: " + e.getMessage());
            return new Wini();
        }
    }
}
