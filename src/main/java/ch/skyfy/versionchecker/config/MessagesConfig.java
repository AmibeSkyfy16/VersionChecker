package ch.skyfy.versionchecker.config;

public class MessagesConfig {
    public final String welcomeMessage;
    public final String wrongVersionMessage;
    public final String missingOnClientMessage;
    public MessagesConfig() {
        this.welcomeMessage = "Welcome, you have the latest version of the modpack (%s). Have a good adventure !";
        this.wrongVersionMessage = "A new version (%s) of the modpack is available. You must install it to play";
        this.missingOnClientMessage = "The VersionChecker mod is missing on the client, you must install it";
    }
}
