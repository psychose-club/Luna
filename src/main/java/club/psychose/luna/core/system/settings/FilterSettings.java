package club.psychose.luna.core.system.settings;

import java.util.ArrayList;
import java.util.HashMap;

public final class FilterSettings {
    private final ArrayList<String> blacklistedWords = new ArrayList<>();
    private final ArrayList<String> whitelistedWords = new ArrayList<>();
    private final HashMap<String, String> bypassDetectionHashMap = new HashMap<>();

    public ArrayList<String> getBlacklistedWords () {
        return this.blacklistedWords;
    }

    public ArrayList<String> getWhitelistedWords () {
        return this.whitelistedWords;
    }

    public HashMap<String, String> getBypassDetectionHashMap () {
        return this.bypassDetectionHashMap;
    }
}