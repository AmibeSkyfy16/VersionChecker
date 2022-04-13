package ch.skyfy.versionchecker.config;

import ch.skyfy.versionchecker.config.core.ConfigData;

import static ch.skyfy.versionchecker.VersionChecker.LOGGER;

/**
 * This class is loaded by reflection
 * This class contains all loaded configurations for the mods
 * Coder can access this code
 */
public class MyConfig {
    public static final ConfigData<VersionConfig> VERSION_CONFIG = ConfigData.build("version.json", VersionConfig.class, false);
    public static final ConfigData<MessagesConfig> MESSAGES_CONFIG = ConfigData.build("messages.json", MessagesConfig.class, true);

    static {
        LOGGER.info(MyConfig.class.getName() + " has been loaded");
    }
}
