package com.example.app.conf.resourceserver;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import org.springframework.core.io.Resource;

@Component
@ConfigurationProperties("security")
public class SecurityProperties {

	private Resource publicKey;

    public Resource getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(Resource publicKey) {
        this.publicKey = publicKey;
    }
}
