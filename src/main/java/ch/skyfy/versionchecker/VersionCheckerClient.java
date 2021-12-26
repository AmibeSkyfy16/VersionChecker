package ch.skyfy.versionchecker;

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
public class VersionCheckerClient extends VersionCheckerBase implements ClientModInitializer {

    public VersionCheckerClient() {}

    @Override
    public void onInitializeClient() {}

    @Override
    protected void registerGlobalReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(new Identifier("versionchecker"), (client, handler, buf, responseSender) -> {
            final var serverAnswer = buf.readString();
            client.execute(() -> {
                if (serverAnswer.equals(config.version))
                    ClientPlayNetworking.send(new Identifier("versionchecker"), PacketByteBufs.create().writeString("valid"));
                else
                    ClientPlayNetworking.send(new Identifier("versionchecker"), PacketByteBufs.create().writeString("unvalid"));
            });
        });
    }

    @Override
    protected void registerEvents() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            // Send the version of the modpack to the server
            // If server doesn't have the same version, player will be kick
            // Adding some delay (10 secondes) otherwise -> server said: timed out
            Executors.newSingleThreadScheduledExecutor(r -> new Thread(r) {{
                setDaemon(true);
            }}).schedule(() -> client.execute(() -> ClientPlayNetworking.send(new Identifier("versionchecker"), PacketByteBufs.create().writeString(config.version))), 10, TimeUnit.SECONDS);
        });
    }

}
