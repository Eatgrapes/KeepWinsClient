package dev.eatgrapes.keepwins;

public class KeepWins {
    public static final KeepWins instance = new KeepWins();
    public static final Client client = new Client();

    public static String ClientName = "KeepWins";
    public static String ClientVersion = "1.0.0";
    
    public static void init() {
        client.init();
    }
}