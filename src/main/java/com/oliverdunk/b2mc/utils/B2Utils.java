package com.oliverdunk.b2mc.utils;

import com.oliverdunk.b2mc.B2MC;
import com.oliverdunk.b2mc.Config;
import com.oliverdunk.jb2.api.B2API;
import com.oliverdunk.jb2.exceptions.B2APIException;
import com.oliverdunk.jb2.models.*;

import java.io.File;
import java.util.List;

public class B2Utils {

    private B2MC b2mc;
    private Config config;

    public B2Utils(B2MC b2MC, Config config){
        this.b2mc = b2MC;
        this.config = config;
    }

    /**
     * Uploads a backup to the B2 cloud.
     *
     * @param session Session used for authorization
     * @param toUpload File which should be uploaded
     */
    public void uploadBackup(B2Session session, File toUpload){
        try{
            List<B2Bucket> buckets = B2API.listBuckets(session);
            for(B2Bucket bucket : buckets) {
                if(!(bucket.getName().equals(config.getBucketName()))) return;
                b2mc.getLogger().info("Deleting old backups before starting upload...");
                deleteOldBackups(session, bucket);
                b2mc.getLogger().info("Continuing upload...");
                B2UploadRequest request = B2API.getUploadURL(session, bucket);
                B2API.uploadFile(request, toUpload, System.currentTimeMillis() + ".zip");
                return;
            }
            B2API.createBucket(session, config.getBucketName(), BucketType.ALL_PRIVATE);
            uploadBackup(session, toUpload);
        }catch(B2APIException ex){
            b2mc.getLogger().info("An error occurred while uploading the backup. (" + ex.getErrorMessage() + ")");
        }
    }

    /**
     * Deletes all backups from a bucket.
     *
     * @param session Session used for authorization
     * @param bucket Bucket which should be cleared
     */
    public void deleteOldBackups(B2Session session, B2Bucket bucket){
        for(B2File file : B2API.listFiles(session, bucket)){
            B2API.deleteFile(session, file);
        }
    }

}
