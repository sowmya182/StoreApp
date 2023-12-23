package com.kosuri.stores.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;


@Getter
@Setter
public class CreateStoreRequest extends RequestEntity<CreateStoreRequest> {
    public CreateStoreRequest(HttpMethod method, URI url) {
        super(method, url);
    }

    private String storeType;
    @NotNull
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String pincode;
    @NotNull
    private String district;
    private String town;
    @NotNull
    private String state;
    private String owner;
    private String ownerAddress;
    private String ownerContact;
    private String secondaryContact;
    private String ownerEmail;
    @NotNull
    private String location;
    private String expirationDate;
    private String storeVerificationStatus;

    @Override
    public String toString() {
        return "CreateStoreRequest{" +
                "storeType='" + storeType + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pincode='" + pincode + '\'' +
                ", district='" + district + '\'' +
                ", town='" + town + '\'' +
                ", state='" + state + '\'' +
                ", owner='" + owner + '\'' +
                ", ownerAddress='" + ownerAddress + '\'' +
                ", ownerContact='" + ownerContact + '\'' +
                ", secondaryContact='" + secondaryContact + '\'' +
                ", ownerEmail='" + ownerEmail + '\'' +
                ", location='" + location + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", storeVerificationStatus='" + storeVerificationStatus + '\'' +
                '}';
    }
}
