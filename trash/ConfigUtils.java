package ch.skyfy.versionchecker.test;


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

import static ch.skyfy.versionchecker.test.BetterConfig.CONFIG_DIRECTORY;

public class ConfigUtils {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * @param relativeFilePath the file path of the config, relative to FabricLoader.getInstance().getConfigDir().resolve("FolderModName")
     * @param <P>              object representing the configuration that we have to save or load
     * @return read config or default config or null if exception
     */
    @SuppressWarnings("UnstableApiUsage")
    public static <P> @Nullable P getOrCreateConfig(Class<P> pClass, String relativeFilePath) {
        var type = TypeToken.of(pClass).getType();
        var configFile = CONFIG_DIRECTORY.resolve(relativeFilePath).toFile();
        P config = null;
        try {
            if (configFile.exists())
                config = get(configFile, type);
            else {
                config = pClass.getDeclaredConstructor().newInstance();
                save(configFile, type, config);
            }
        } catch (IOException | JsonIOException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();

        }
        return config;
    }

    public static <P> P get(File file, Type type) throws IOException {
        try (var reader = new FileReader(file)) {
            return gson.fromJson(reader, type);
        }
    }

    public static <P> void save(File file, Type type, P p) throws IOException {
        try (var writer = new FileWriter(file)) {
            gson.toJson(p, type, writer);
        }
    }

}
