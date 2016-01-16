package com.oliverdunk.b2mc;

import com.oliverdunk.jb2.api.B2API;
import com.oliverdunk.jb2.exceptions.B2APIException;
import com.oliverdunk.jb2.models.B2Session;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Optional;

public class Config {

    private String accountID;
    private String applicationKey;
    private String bucketName;

    /**
     * Loads the configuration file into memory.
     *
     * @param config Config file to load from
     */
    public void loadConfig(FileConfiguration config){
        accountID = config.getString("b2.accountID");
        applicationKey = config.getString("b2.applicationKey");
        bucketName = config.getString("b2.bucketName");
    }

    /**
     * Creates a session using the account information stored in memory.
     *
     * @return An authorized account, present if the API call was successful
     */
    public Optional<B2Session> createSession(){
        try{
            return Optional.of(B2API.authorizeAccount(accountID, applicationKey));
        }catch(B2APIException ex){
            return Optional.empty();
        }
    }

    public String getBucketName(){
        return bucketName;
    }

}