package com.arete.korbly.modules.shared.generics;

import com.amazonaws.services.s3.model.Bucket;

import java.io.IOException;
import java.util.List;

public interface BucketService {

    /**
     * Gets a list f buckets
     * @return a list of buckets
     */
    List<Bucket> getBucketList();

    /**
     * Check if a give bucket name is valid
     * @param bucketName
     * @return boolean
     */
    boolean validateBucket(String bucketName);

    /**
     * Download a given object from the bucket
     * @param bucketName
     * @param objectName
     * @throws IOException
     */
    void getObjectFromBucket(String bucketName, String objectName) throws IOException;

    /**
     * Upload given file as object to an S3 bucket
     * @param bucketName
     * @param objectName
     * @param filePathToUpload
     */
    void putObjectIntoBucket(String bucketName, String objectName, String filePathToUpload);

    /**
     * Create a bucket with the provided name (throws exception if the bucket already exists)
     * @param bucket
     */
    void createBucket(String bucket);
}
