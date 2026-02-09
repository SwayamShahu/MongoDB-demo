package com.app.analytics.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document
public class Otp {
    @Id
    private String id;
    private String mail;
    private long expireTime;
    private boolean isUsed;
    private String otp;
}
