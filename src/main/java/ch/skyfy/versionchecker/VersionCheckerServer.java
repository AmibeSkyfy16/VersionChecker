package ch.skyfy.versionchecker;

import ch.skyfy.versionchecker.config.MyConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static ch.skyfy.versionchecker.VersionChecker.clientAnswered;
import static ch.skyfy.versionchecker.config.MyConfig.MESSAGES_CONFIG;

@Environment(EnvType.SERVER)
public class VersionCheckerServer {

    /**
     * Each time a player joins the server, a thread with a delay of 60 seconds is started.
     * If after 60 seconds the server still hasn't received a response about the client version, the player is kicked
     */
    public static void initialize() {
        ServerPlayNetworking.registerGlobalReceiver(new Identifier("versionchecker"), (server, player, handler, buf, responseSender) -> {
            final var config = MyConfig.VERSION_CONFIG.config();
            final var clientVersion = buf.readString();
            clientAnswered.set(true);
            server.execute(() -> {
                if (clientVersion.equals(config.version))
                    player.sendMessage(Text.of(MESSAGES_CONFIG.config().welcomeMessage.formatted(config.version)), false);
                else
                    player.networkHandler.disconnect(Text.of(MESSAGES_CONFIG.config().wrongVersionMessage.formatted(config.version)));
            });
        });
        ServerPlayConnectionEvents.JOIN.register((handler, sender, client) -> Executors.newSingleThreadScheduledExecutor(r -> new Thread(r) {{
            setDaemon(true);
        }}).schedule(() -> {
            if (!clientAnswered.get())
                client.execute(() -> handler.disconnect(Text.of(MESSAGES_CONFIG.config().missingOnClientMessage)));
        }, 60, TimeUnit.SECONDS));
    }
}
