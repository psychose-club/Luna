package club.psychose.luna.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
    public static final String VERSION = "1.0.0";
    public static final String BUILD = "1";

    public static Path getLunaFolderPath (String additionalPath) {
        return additionalPath != null ? StringUtils.getOSPath(Paths.get(System.getProperty("user.home") + "\\psychose.club\\Luna\\" + additionalPath)) : StringUtils.getOSPath(Paths.get(System.getProperty("user.home") + "\\psychose.club\\Luna\\"));
    }
}