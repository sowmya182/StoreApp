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

    @NotNull
    private String userFullName;
    @NotNull
    private String userPhoneNumber;
    @NotNull
    private String userEmail;
    @NotNull
    private String status;
    @NotNull
    private String addedBy;
    @NotNull
    private String storeAdminEmail;
    @NotNull
    private String storeAdminMobile;
    @NotNull
    private String role;
    @NotNull
    private String store;
    @NotNull
    private String password;
	@NotNull
	private String userType;
}
