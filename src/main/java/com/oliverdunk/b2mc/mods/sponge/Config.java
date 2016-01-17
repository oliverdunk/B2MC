package com.oliverdunk.b2mc.mods.sponge;

import com.google.inject.Inject;
import com.oliverdunk.b2mc.api.AbstractConfig;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Config extends AbstractConfig<Void> {

    @Inject
    Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configPath;

    private ConfigurationNode configuration;

    /**
     * Instructs ConfigurationUtilities to attempt to load its config
     */
    public void loadConfig(Void nothing){
        try {
            //Create if it does not already exist
            File potentialFile = new File(configPath.toUri());
            if(!potentialFile.exists()) potentialFile.createNewFile();

            //Load, add new defaults and then overwrite
            ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder().setPath(configPath).build();
            configuration = configLoader.load();
            applyDefaults(configuration);

            accountID = configuration.getNode("b2").getNode("accountID").getString();
            applicationKey = configuration.getNode("b2").getNode("applicationKey").getString();
            bucketName = configuration.getNode("b2").getNode("bucketName").getString();

            configLoader.save(configuration);
        }catch(IOException exception){
            logger.warn("Unable to load config. B2MC will not work as intended.");
        }
    }

    private void applyDefaults(ConfigurationNode node){
        applyDefault(node.getNode("b2").getNode("accountID"), "changeme");
        applyDefault(node.getNode("b2").getNode("applicationKey"), "changeme");
        applyDefault(node.getNode("b2").getNode("bucketName"), "changeme");
    }

    private void applyDefault(ConfigurationNode node, Object value){
        if(node.getValue() == null) node.setValue(value);
    }

    /**
     * Gets the configuration which is loaded into memory.
     *
     * @return The root configuration node
     */
    public ConfigurationNode getConfig(){
        return configuration;
    }

}
