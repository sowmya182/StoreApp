package com.kosuri.stores.model.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;

@Getter
@Setter
public class DiagnosticCenterRequest extends RequestEntity<DiagnosticCenterRequest> {


    private String serviceId;

    private String userId;

    private String  phoneNumber;

    private String address;

    private String location;

    private String userEmailId;

    private String businessName;

    private String serviceName;

    private String price;

    private String description;

    private String serviceCategory;

    private String storeId;
    public DiagnosticCenterRequest(HttpMethod method, URI url) {
        super(method, url);
    }
}

