package ch.skyfy.versionchecker.test;

public class MyConfig {
    public static final ConfigData<WorldGenerationConfig> WorldGenerationConfig = new ConfigData<>("worldgen.json", WorldGenerationConfig.class);


    static {
        System.out.println("CALLED STATIC BLOC");
    }

    public static void loadConfig(){

    }

}
