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

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public void setUserPhoneNumber(String userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public void setStoreAdminEmail(String storeAdminEmail) {
		this.storeAdminEmail = storeAdminEmail;
	}

	public void setStoreAdminMobile(String storeAdminMobile) {
		this.storeAdminMobile = storeAdminMobile;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
}
