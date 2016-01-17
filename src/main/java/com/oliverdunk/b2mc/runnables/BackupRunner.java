package com.oliverdunk.b2mc.runnables;

import com.oliverdunk.b2mc.api.AbstractConfig;
import com.oliverdunk.b2mc.utils.B2Utils;
import com.oliverdunk.b2mc.utils.ZipUtils;
import com.oliverdunk.jb2.models.B2Session;

import java.io.*;
import java.util.Optional;

public abstract class BackupRunner implements Runnable {

    private final AbstractConfig config;

    /**
     * Constructor for the BackupRunner.
     *
     * @param config Configuration containing authentication details in memory
     */
    public BackupRunner(AbstractConfig config){
        this.config = config;
    }

    public void run() {
        log("Running backup...");

        Optional<B2Session> sessionOptional = config.createSession();
        if(sessionOptional.isPresent()){
            setAutoSave(false);
            boolean zipCreated = ZipUtils.createZipFromDirectory(new File(System.getProperty("user.dir")), new File("backup.zip"));
            if(zipCreated) {
                log("Backup created, uploading to B2...");

                B2Utils utils = new B2Utils(this, config);
                utils.uploadBackup(sessionOptional.get(), new File("backup.zip"));
                setAutoSave(true);

                log("Backup completed!");
            }else{
                log("Zip creation failed, backup aborted.");
            }
        }else{
            log("Unable to authenticate with the B2 API!");
        }
    }

    public abstract void log(String message);
    public abstract void setAutoSave(boolean autoSave);

}