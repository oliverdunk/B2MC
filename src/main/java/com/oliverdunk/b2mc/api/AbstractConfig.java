package com.oliverdunk.b2mc.api;

import com.oliverdunk.jb2.api.B2API;
import com.oliverdunk.jb2.exceptions.B2APIException;
import com.oliverdunk.jb2.models.B2Session;

import java.util.Optional;

public abstract class AbstractConfig<T> {

    protected String accountID;
    protected String applicationKey;
    protected String bucketName;

    public abstract void loadConfig(T config);

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
