package com.api.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Service;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import java.io.InputStream;

@Service
public class StorageService {

    public void uploadFile(String bucket, String fileName, InputStream inputStream) throws AmazonServiceException{
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, inputStream, null)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
        getClient().putObject(putObjectRequest);
    }

    public void deleteFile(String bucket, String fileName) throws AmazonServiceException{
        getClient().deleteObject(bucket, fileName);
    }

    public AmazonS3 getClient(){
        return AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_2).build();
    }

    public String getPath(String bucket, Region region, String key){
        if(key == null || key.isEmpty()) return null;
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region.toString(), key);
    }
}
