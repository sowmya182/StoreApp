package com.kosuri.stores.dao;
//
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tab_store_user_login")
public class TabStoreUserEntity {
    private @Column(name = "store_category") String type;

	@Id
	@NotNull
	private @Column(name = "user_id") String userId;
    private @Column(name = "store_user_full_name") String name;
    @NotNull
    private @Column(name = "store_user_phonenumber", unique=true) String storeUserContact;
    @NotNull
    private @Column(name = "store_user_emailid") String storeUserEmail;
    private @Column(name = "registraion_date") String registrationDate;
    private @Column(name = "addedby") String addedBy;
    private @Column(name = "store_admin_email") String storeAdminEmail;
    private @Column(name = "store_admin_mobile") String storeAdminContact;
    private @Column(name = "status") String status;
    private @Column(name = "password") String password;
    private @Column(name = "user_type") String userType;
}
