package ch.skyfy.versionchecker;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Environment(EnvType.SERVER)
public class VersionCheckerServer extends VersionCheckerBase implements Initializer{

    private static class VersionCheckerServerHolder {
        public static final VersionCheckerServer INSTANCE;

        static {
            INSTANCE = new VersionCheckerServer();
            INSTANCE.init();
        }
    }

    public static void initialize() {
        VersionCheckerServer.VersionCheckerServerHolder.INSTANCE.init();
    }

    public VersionCheckerServer() {}

    @Override
    protected void registerGlobalReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(new Identifier("versionchecker"), (server, player, handler, buf, responseSender) -> {
            final var clientAnswer = buf.readString();
            clientAnswered.set(true);
            server.execute(() -> {
                if (clientAnswer.equals(config.version))
                    player.sendMessage(Text.of("Welcome, you have the latest version of the modpack(" + config.version + "). Good Adventure !"), false);
                else
                    player.networkHandler.disconnect(Text.of("A new version(" + config.version + ") of the modpack is available. You must install it to play"));
            });
        });
    }

    @Override
    protected void registerEvents() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            // If the player who logs in has not installed the modpack, or does not have the correct version of the modpack, he will be kicked after 60 secondes
            Executors.newSingleThreadScheduledExecutor(r -> new Thread(r) {{
                setDaemon(true);
            }}).schedule(() -> {
                if (!clientAnswered.get())
                    client.execute(() -> handler.disconnect(Text.of("You must install the correct version of the modpack to play")));
            }, 60, TimeUnit.SECONDS);
        });
    }

}
