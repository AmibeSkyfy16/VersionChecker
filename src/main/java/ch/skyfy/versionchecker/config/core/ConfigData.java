package ch.skyfy.versionchecker.config.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

public record ConfigData<C>(C config) {
    public static <C> @NotNull ConfigData<C> build(String relativeFilePath, Class<C> cClass, boolean serverOnly) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT && serverOnly) return new ConfigData<>(null);

        var c = ConfigUtils.getOrCreateConfig(cClass, relativeFilePath);
        if (c == null) throw new NullPointerException("Config is null, pls remove all config files and restart server");
        return new ConfigData<>(c);
    }
}
