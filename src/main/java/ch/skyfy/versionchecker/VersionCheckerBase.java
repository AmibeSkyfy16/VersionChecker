package ch.skyfy.versionchecker;

import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.Nullable;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static ch.skyfy.versionchecker.VersionChecker.MOD_CONFIG_DIR;

public abstract class VersionCheckerBase {

    protected final Config config;

    protected final AtomicBoolean clientAnswered = new AtomicBoolean(false);

    public VersionCheckerBase() {
        this.config = createConfig();
        registerGlobalReceiver();
        registerEvents();
    }


    protected abstract void registerGlobalReceiver();

    protected abstract void registerEvents();

    protected @Nullable Config createConfig() {
        var configDir = MOD_CONFIG_DIR.toFile();
        if (!configDir.exists())
            if (!configDir.mkdir()) return null;

        var gson = new GsonBuilder().setPrettyPrinting().create();
        var configFile = MOD_CONFIG_DIR.resolve("config.json").toFile();
        var config = new Config("putTheVersionHere");
        try {
            if (!configFile.exists()) {
                try (var writer = new FileWriter(configFile)) {
                    gson.toJson(config, writer);
                }
            } else {
                try (var reader = new FileReader(configFile)) {
                    config = gson.fromJson(reader, Config.class);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return config;
    }
}
