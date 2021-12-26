package ch.skyfy.versionchecker;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class VersionChecker implements ModInitializer {

    public static final Path MOD_CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("VersionChecker");

    @Override
    public void onInitialize() {
        if(FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
            VersionCheckerServer.initialize();
    }

}
