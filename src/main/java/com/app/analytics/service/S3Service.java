package com.app.analytics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RequiredArgsConstructor
@Service
public class S3Service {
    private final S3Client s3Client;
    private final String bucket = "analytics-bucket";

    public void upload(String filename, byte[] data){
        s3Client.putObject(PutObjectRequest.builder()
                .bucket(bucket)
                .key(filename)
                .build(), RequestBody.fromBytes(data));
    }

    public byte[] download(String filename){
        return s3Client.getObjectAsBytes(
                GetObjectRequest
                        .builder()
                        .bucket(bucket)
                        .key(filename)
                        .build()
        ).asByteArray();
    }
}
