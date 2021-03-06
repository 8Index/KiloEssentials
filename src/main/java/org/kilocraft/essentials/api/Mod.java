package org.kilocraft.essentials.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class Mod {
    private static Logger logger = LogManager.getFormatterLogger();
    private static Properties properties = new Properties();
    private static Properties lang = new Properties();

    private static String version;

    public Mod() {
        try {
            properties.load(Mod.class.getClassLoader().getResourceAsStream("mod.properties"));
            lang.load(Mod.class.getClassLoader().getResourceAsStream("assets/Lang.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Thimble.disabledCommandPerms.add("item");
    }

    public static Logger getLogger() {
        return logger;
    }

    public static Properties getProperties() {
        return properties;
    }

    public static Properties getLang() {
        return lang;
    }

    public static String getVersion() {
        return properties.getProperty("version");
    }

    public static String getMappingsVersion() {
        return properties.getProperty("fabric_yarn_mappings");
    }

    public static String getLoaderVersion() {
        return properties.getProperty("fabric_loader_version");
    }

    public static String getMinecraftVersion() {
        return getProperties().getProperty("minecraft_version");
    }

    public static boolean isDebugEnabled() {
        return Boolean.parseBoolean(getProperties().getProperty("enabledebug"));
    }


}
