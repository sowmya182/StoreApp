package com.kosuri.stores.model.request;

import com.kosuri.stores.model.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;

@Getter
@Setter
public class AddTabStoreUserRequest extends RequestEntity<AddTabStoreUserRequest> {
    public AddTabStoreUserRequest(HttpMethod method, URI url) {
        super(method, url);
    }


    private String userFullName;
    @NotNull
    private String userPhoneNumber;
    @NotNull
    private String userEmail;

    private String status;

    private String addedBy;

    private String storeAdminEmail;

    private String storeAdminMobile;

    private String role;

    private String store;

    @NotNull
    private String password;

	private String userType;
}
