package dev.eatgrapes.keepwins.config;

import com.google.gson.JsonObject;

import java.io.File;

public abstract class Config {
    public String name;
    protected File configFile;

    public Config(String configName, String fileName) {
        this.name = configName;
        this.configFile = new File(ConfigManager.rootDir, fileName);
    }

    public abstract void loadConfig(JsonObject object);

    public abstract JsonObject saveConfig();
}
