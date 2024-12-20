package com.radtimes.rad_times_server.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class AmazonClientUtil {

    private final Logger logger = LoggerFactory.getLogger(AmazonClientUtil.class);

    private AmazonS3 s3client;

    @Value("${aws.bucketName}")
    private String bucketName;
    @Value("${aws.accessKey}")
    private String accessKey;
    @Value("${aws.secretKey}")
    private String secretKey;
    @Value("${aws.region}")
    private String awsRegion;

    @PostConstruct
    private void initializeAmazonClient() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        this.s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(awsRegion).build();
        createBucket();
    }


    public void uploadFileToBucket(String fileName, File file, String folderToUpload) {
        logger.info("Uploading file {} to {}", fileName, folderToUpload);
        s3client.putObject(new PutObjectRequest(bucketName, folderToUpload + "/" + fileName, file));
    }


    public void deleteFileFromBucket(String filename, String folderName) {
        logger.info("Deleting file {} from {}", filename, folderName);
        DeleteObjectRequest delObjReq = new DeleteObjectRequest(bucketName, folderName + "/" + filename);
        s3client.deleteObject(delObjReq);
    }


    public void deleteMultipleFilesFromBucket(List<String> files) {
        DeleteObjectsRequest delObjReq = new DeleteObjectsRequest(bucketName)
                .withKeys(files.toArray(new String[0]));
        logger.info("Deleting files...");
        s3client.deleteObjects(delObjReq);
    }


    public File getFileFromBucket(String filename, String folderName) {
        InputStream inputStream = getFileInputStream(filename, folderName);
        File file = new File(filename);
        try {
            FileUtils.copyInputStreamToFile(inputStream, file);
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return file;
        }
        return file;
    }


    public InputStream getFileInputStream(String filename, String folderName) {
        S3Object s3object = s3client.getObject(bucketName, folderName + "/" + filename);
        return s3object.getObjectContent();
    }


    private void createBucket() {
        if (s3client.doesBucketExistV2(bucketName)) {
            return;
        }
        try {
            logger.info("Creating bucket {}", bucketName);
            s3client.createBucket(bucketName);
        } catch (Exception e) {
            logger.error((ExceptionUtils.getStackTrace(e)));
        }
    }
}
