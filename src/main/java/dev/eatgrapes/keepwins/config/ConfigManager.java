package dev.eatgrapes.keepwins.config;

import dev.eatgrapes.keepwins.KeepWins;
import dev.eatgrapes.keepwins.util.MinecraftInstance;
import dev.eatgrapes.keepwins.util.misc.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager extends MinecraftInstance {
    public static final File rootDir = new File(mc.mcDataDir, KeepWins.ClientName);
    public static final File cacheDir = new File(rootDir, "cache");
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final List<Config> configList;

    public ConfigManager() {
        configList = new ArrayList<>();
        if (!rootDir.exists()) rootDir.mkdir();
        if (!cacheDir.exists()) cacheDir.mkdir();
    }

    public void registerConfigs() {
      //  configList.add(new ModuleConfig());
     //   configList.add(new WidgetConfig());
    }

    public void loadConfigs() {
        for (Config config : configList) {
            if (!config.configFile.exists()) continue;
            try {
                JsonObject object = ConfigManager.gson.fromJson(new FileReader(config.configFile), JsonObject.class);
                config.loadConfig(object);
            } catch (FileNotFoundException e) {
                Logger.error("Failed to load config " + config.name);
            }
        }
    }

    public void saveConfigs() {
        configList.forEach(it -> {
            if (!it.configFile.exists()) {
                try {
                    it.configFile.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        for (Config config : configList) {
            JsonObject object = config.saveConfig();
            try {
                PrintWriter pw = new PrintWriter(config.configFile);
                pw.write(ConfigManager.gson.toJson(object));
                pw.flush();
                pw.close();
            } catch (FileNotFoundException e) {
                Logger.error("Failed to write " + config.name + " config.");
            }
        }
    }
}