package ch.skyfy.versionchecker;

import ch.skyfy.versionchecker.config.MyConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.util.Identifier;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Environment(EnvType.CLIENT)
public class VersionCheckerClient implements ClientModInitializer {

    /**
     * When a player joins a multiplayer server,
     * the version of the modpack is sent to the server so that the server can allow the player to stay or not.
     */
    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.getServer() != null && !client.getServer().isDedicated()) return; // ignore singleplayer
            final var config = MyConfig.VERSION_CONFIG.config();
            Executors.newSingleThreadScheduledExecutor(r -> new Thread(r) {{
                setDaemon(true);
            }}).schedule(() -> client.execute(() -> ClientPlayNetworking.send(new Identifier("versionchecker"), PacketByteBufs.create().writeString(config.version))), 10, TimeUnit.SECONDS);
        });
    }
}
