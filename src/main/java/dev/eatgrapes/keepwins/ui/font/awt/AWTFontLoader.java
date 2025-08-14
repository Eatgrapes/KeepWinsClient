package dev.eatgrapes.keepwins.ui.font.awt;

import dev.eatgrapes.keepwins.util.misc.Logger;

import java.util.HashMap;
import java.util.Map;


public class AWTFontLoader {
    private static final Map<Integer, AWTAWTFontRenderer> miSans = new HashMap<>();
    private static final Map<Integer, AWTAWTFontRenderer> miSansBold = new HashMap<>();
    private static final Map<Integer, AWTAWTFontRenderer> icon = new HashMap<>();

    //These are for the icon font for ease of access
    public final static String
            BUG = "a",
            LIST = "b",
            BOMB = "c",
            EYE = "d",
            PERSON = "e",
            WHEELCHAIR = "f",
            SCRIPT = "g",
            SKIP_LEFT = "h",
            PAUSE = "i",
            PLAY = "j",
            SKIP_RIGHT = "k",
            SHUFFLE = "l",
            INFO = "m",
            SETTINGS = "n",
            CHECKMARK = "o",
            XMARK = "p",
            TRASH = "q",
            WARNING = "r",
            FOLDER = "s",
            LOAD = "t",
            SAVE = "u",
            UPVOTE_OUTLINE = "v",
            UPVOTE = "w",
            DOWNVOTE_OUTLINE = "x",
            DOWNVOTE = "y",
            DROPDOWN_ARROW = "z",
            PIN = "s",
            EDIT = "A",
            SEARCH = "B",
            UPLOAD = "C",
            REFRESH = "D",
            ADD_FILE = "E",
            STAR_OUTLINE = "F",
            STAR = "G";

    /**
     * Register international font MiSans
     */
    public static void registerFonts() {
        miSans(15);
        miSans(17);
        icon(15);
        icon(17);
        icon(28);
    }

    public static AWTAWTFontRenderer icon(int size) {
        return get(icon, size, "icon", false);
    }

    public static AWTAWTFontRenderer miSans(int size) {
        return get(miSans, size, "misans", true);
    }

    private static AWTAWTFontRenderer get(Map<Integer, AWTAWTFontRenderer> map, int size, String name, boolean chinese) {
        if (!map.containsKey(size)) {
            Logger.info("Registering font " + name + (chinese ? " including Chinese." : ".") + " Size: " + size);
            java.awt.Font font = FontUtil.getResource("KeepWins/font/" + name + ".ttf", size);
            if (font != null) {
                map.put(size, new AWTAWTFontRenderer(font, chinese));
            }
        }

        return map.get(size);
    }
}