package ch.skyfy.versionchecker.config.core;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.nio.file.InvalidPathException;

import static ch.skyfy.versionchecker.config.core.BetterConfig.CONFIG_DIRECTORY;

public class ConfigUtils {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * @param relativeFilePath the file path of the config, relative to FabricLoader.getInstance().getConfigDir().resolve("FolderModName")
     * @param <P>              An object representing the configuration that we have to save or load
     * @return a config (return the default config if json file not exist) or null if exception
     */
    @SuppressWarnings("UnstableApiUsage")
    public static <P> @Nullable P getOrCreateConfig(Class<P> pClass, String relativeFilePath) {
        var type = TypeToken.of(pClass).getType();
        P config;
        try {
            var configFile = CONFIG_DIRECTORY.resolve(relativeFilePath).toFile();
            if (configFile.exists())
                config = get(configFile, type);
            else {
                config = pClass.getDeclaredConstructor().newInstance();
                save(configFile, type, config);
            }
        } catch (IOException | JsonIOException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | InvalidPathException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
        return config;
    }

    public static <P> P get(File file, Type type) throws IOException {
        try (var reader = new FileReader(file)) {
            return gson.fromJson(reader, type);
        }
    }

    public static <P> void save(File file, Type type, P p) throws IOException {
        createNonexistentDirectories(file.getParentFile());
        try (var writer = new FileWriter(file)) {
            gson.toJson(p, type, writer);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void createNonexistentDirectories(File file) {
        file.mkdirs();
    }

}
