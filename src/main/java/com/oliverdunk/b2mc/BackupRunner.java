package com.oliverdunk.b2mc;

import com.oliverdunk.b2mc.utils.B2Utils;
import com.oliverdunk.b2mc.utils.ZipUtils;
import com.oliverdunk.jb2.api.B2API;
import com.oliverdunk.jb2.exceptions.B2APIException;
import com.oliverdunk.jb2.models.B2Bucket;
import com.oliverdunk.jb2.models.B2Session;
import com.oliverdunk.jb2.models.B2UploadRequest;
import com.oliverdunk.jb2.models.BucketType;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.List;
import java.util.Optional;

public class BackupRunner extends BukkitRunnable {

    private final B2MC b2mc;
    private final Config config;

    /**
     * Constructor for the BackupRunner.
     *
     * @param b2MC Main plugin instance
     * @param config Configuration containing authentication details in memory
     */
    public BackupRunner(B2MC b2MC, Config config){
        this.b2mc = b2MC;
        this.config = config;
    }

    @Override
    public void run() {
        b2mc.getLogger().info("Running backup...");

        Optional<B2Session> sessionOptional = config.createSession();
        if(sessionOptional.isPresent()){
            setAutoSave(false);
            boolean zipCreated = ZipUtils.createZipFromDirectory(new File(System.getProperty("user.dir")), new File("backup.zip"));
            if(zipCreated) {
                b2mc.getLogger().info("Backup created, uploading to B2...");

                B2Utils utils = new B2Utils(b2mc, config);
                utils.uploadBackup(sessionOptional.get(), new File("backup.zip"));
                setAutoSave(true);

                b2mc.getLogger().info("Backup completed!");
            }else{
                b2mc.getLogger().info("Zip creation failed, backup aborted.");
            }
        }else{
            b2mc.getLogger().info("Unable to authenticate with the B2 API!");
        }
    }

    private void setAutoSave(boolean autoSave){
        for(World world : b2mc.getServer().getWorlds()){
            //If enabling autosave, catch up by forcing a manual save
            if(autoSave) world.save();
            world.setAutoSave(autoSave);
        }
    }

}