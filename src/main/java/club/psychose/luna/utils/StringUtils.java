package club.psychose.luna.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class StringUtils {
    // Prints an empty line in the console.
    public static void printEmptyLine () {
        System.out.println(" ");
    }

    // These methods are for debugging.
    public static void debug (String output) {
        System.out.println(getDateAndTime("CONSOLE") + " | [Luna]: " + output);
    }

    // This method returns a date and time string.
    public static String getDateAndTime (String formatMode) {
        Date date = new Date();

        DateFormat dateFormat = switch (formatMode) {
            case "CONSOLE" -> new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            case "LOG" -> new SimpleDateFormat("dd-MM-yyyy HH-mm-ss-SSS");
            default -> new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        };

        return dateFormat.format(date);
    }

    // This returns a path with the valid file separators for the OS.
    public static Path getOSPath (Path path) {
        return Paths.get(path.toString().trim().replace("\\", File.separator));
    }
}