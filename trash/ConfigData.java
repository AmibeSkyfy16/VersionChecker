package ch.skyfy.versionchecker.test;

public class ConfigData<C> {
    private String relativeFilePath;
    private Class<C> cClass;
    public C config;
    public ConfigData(String relativeFilePath, Class<C> cClass) {
        this.relativeFilePath = relativeFilePath;
        this.cClass = cClass;
        config = ConfigUtils.getOrCreateConfig(cClass, relativeFilePath);
    }
}
