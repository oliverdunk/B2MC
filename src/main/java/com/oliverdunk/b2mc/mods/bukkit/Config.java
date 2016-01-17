package com.oliverdunk.b2mc.mods.bukkit;

import com.oliverdunk.jb2.api.B2API;
import com.oliverdunk.jb2.exceptions.B2APIException;
import com.oliverdunk.jb2.models.B2Session;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Optional;

public class Config extends com.oliverdunk.b2mc.api.AbstractConfig<FileConfiguration> {

    /**
     * Loads the configuration file into memory.
     *
     * @param config Config file to load from
     */
    public void loadConfig(FileConfiguration config){
        this.accountID = config.getString("b2.accountID");
        this.applicationKey = config.getString("b2.applicationKey");
        this.bucketName = config.getString("b2.bucketName");
    }

}