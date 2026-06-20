package com.satecho.agrosafe.platform.apiservice.iot.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class DeviceCredentialsEmbeddable {
    @Column(name = "api_key", length = 64)
    private String apiKey;
    @Column(name = "certificate_thumbprint", length = 128)
    private String certificateThumbprint;
}
