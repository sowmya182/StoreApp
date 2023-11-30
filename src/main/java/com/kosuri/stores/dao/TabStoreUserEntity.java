package com.kosuri.stores.dao;
//
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "tab_store_user_login")
public class TabStoreUserEntity {
    private @Column(name = "store_category") String type;
    
    private @Column(name = "store_user_full_name") String name;
    @NotNull
    private @Column(name = "store_user_phonenumber", unique=true) String storeUserContact;
    @Id
    @NotNull
    private @Column(name = "store_user_emailid") String storeUserEmail;
    private @Column(name = "registration_date") String registrationDate;
    private @Column(name = "addedby") String addedBy;
    private @Column(name = "store_admin_email") String storeAdminEmail;
    private @Column(name = "store_admin_mobile") String storeAdminContact;
    private @Column(name = "status") String status;
    private @Column(name = "password") String password;
    private @Column(name = "user_type") String userType;
    
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStoreUserContact() {
		return storeUserContact;
	}
	public void setStoreUserContact(String storeUserContact) {
		this.storeUserContact = storeUserContact;
	}
	public String getStoreUserEmail() {
		return storeUserEmail;
	}
	public void setStoreUserEmail(String storeUserEmail) {
		this.storeUserEmail = storeUserEmail;
	}
	public String getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
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
	public String getStoreAdminContact() {
		return storeAdminContact;
	}
	public void setStoreAdminContact(String storeAdminContact) {
		this.storeAdminContact = storeAdminContact;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
}
