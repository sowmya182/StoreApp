package com.kosuri.stores.model.request;

import com.kosuri.stores.model.enums.Role;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import java.net.URI;

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
    
	public String getUserFullName() {
		return userFullName;
	}
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}
	public String getUserPhoneNumber() {
		return userPhoneNumber;
	}
	public void setUserPhoneNumber(String userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAddedBy() {
		return addedBy;
	}
	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}
	public String getStoreAdminEmail() {
		return storeAdminEmail;
	}
	public void setStoreAdminEmail(String storeAdminEmail) {
		this.storeAdminEmail = storeAdminEmail;
	}
	public String getStoreAdminMobile() {
		return storeAdminMobile;
	}
	public void setStoreAdminMobile(String storeAdminMobile) {
		this.storeAdminMobile = storeAdminMobile;
	}
	public String getStore() {
		return store;
	}
	
	public void setStore(String store) {
		this.store = store;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
    
    
}
