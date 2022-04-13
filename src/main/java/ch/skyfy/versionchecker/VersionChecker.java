package ch.skyfy.versionchecker;

import ch.skyfy.versionchecker.config.MyConfig;
import ch.skyfy.versionchecker.config.core.BetterConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

public class VersionChecker implements ModInitializer {

    public static final String MOD_ID = "versionchecker";

    public static final Logger LOGGER = LogManager.getLogger("VersionChecker");

    public static final AtomicBoolean clientAnswered = new AtomicBoolean(false);

    @Override
    public void onInitialize() {
        if (BetterConfig.initialize(new Class[]{MyConfig.class})) return;
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
            VersionCheckerServer.initialize();
    }

}
