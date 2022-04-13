package ch.skyfy.versionchecker.test;

import net.fabricmc.loader.api.FabricLoader;

import java.io.ObjectStreamClass;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static ch.skyfy.versionchecker.VersionCheckerBase.MOD_ID;

public class BetterConfig {

    public static Path CONFIG_DIRECTORY = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID);

    private boolean broke = false;

    private Map<? super Object, String> allConfigs = new HashMap<>();

    private void createConfigDirectory() {
        var file = CONFIG_DIRECTORY.toFile();
        if (!file.exists()) broke = !file.mkdir();


    }

    /**
     * concept
     * <p>
     * Each mod will have its own folder in the config folder of the server which will contain the different configurations
     *
     * @return
     */
    public boolean build() {
        createConfigDirectory();
        try {
            Class.forName("ch.skyfy.versionchecker.test.MyConfig");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//        try {
//            Method method = ObjectStreamClass.class.getDeclaredMethod("hasStaticInitializer", Class.class);
//            method.setAccessible(true);
//            return (boolean) method.invoke(null, MyConfig.class);
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }

//        ConfigData<WorldGenerationConfig> WorldGenerationConfig = new ConfigData<>("config.json", WorldGenerationConfig.class);


        return false;
    }

    public void addConfig(Object config, String relativePath) {
        allConfigs.put(config, relativePath);
    }

}
