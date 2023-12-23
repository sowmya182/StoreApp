package com.kosuri.stores.handler;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.kosuri.stores.constant.StoreConstants;
import com.kosuri.stores.dao.StoreEntity;
import com.kosuri.stores.dao.TabStoreUserEntity;
import com.kosuri.stores.exception.APIException;
import com.kosuri.stores.model.request.AddTabStoreUserRequest;
import com.kosuri.stores.model.request.AddUserRequest;
import com.kosuri.stores.model.request.LoginUserRequest;
import com.kosuri.stores.model.response.LoginUserResponse;

import com.kosuri.stores.request.OTPRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserHandler {
    @Autowired
    private RepositoryHandler repositoryHandler;

    @Autowired
    private StoreHandler storeHandler;

    @Autowired
    private RoleHandler roleHandler;


    public boolean addUser(AddUserRequest request) throws Exception {
        if(!repositoryHandler.validateuser(request)){
            return false;
        }
        StoreEntity userStoreEntity = getEntityFromUserRequest(request);
        try {
            repositoryHandler.addUser(userStoreEntity, request);
        } catch (DataIntegrityViolationException e) {
            throw new Exception(e.getCause().getCause().getMessage());
        }
        return true;
    }
    public boolean addUser(AddTabStoreUserRequest request) throws Exception {
        if (!repositoryHandler.validateStoreUser(request)) {
            return false;
        }
        TabStoreUserEntity userStoreEntity = getEntityFromStoreUserRequest(request);
        boolean isUserAdded;
        try {
            isUserAdded = repositoryHandler.addStoreUser(userStoreEntity, request);

        } catch (DataIntegrityViolationException e) {
            throw new Exception(e.getCause().getCause().getMessage());
        }
        return isUserAdded;
    }

    private TabStoreUserEntity getEntityFromStoreUserRequest(AddTabStoreUserRequest request) {
        TabStoreUserEntity storeEntity = new TabStoreUserEntity();
        
        storeEntity.setStatus(request.getStatus());
        storeEntity.setName(request.getUserFullName());
        storeEntity.setStoreUserEmail(request.getUserEmail());
        storeEntity.setStoreUserContact(request.getUserPhoneNumber());
        storeEntity.setType(request.getStore());
        storeEntity.setStoreAdminContact(request.getStoreAdminMobile());
        storeEntity.setStoreAdminEmail(request.getStoreAdminEmail());
        storeEntity.setPassword(getEncryptedPassword(request.getPassword()));
        storeEntity.setUserType(request.getUserType());
        storeEntity.setRegistrationDate(LocalDateTime.now());
        storeEntity.setUserId(genereateUserId());

        //setting dummy parameters.
		/*
		 * if(request.getUserPhoneNumber() != null){ storeEntity.setId("DUMMY" +
		 * request.getUserPhoneNumber());
		 *
		 * }else{ storeEntity.setId("DUMMY" + request.getUserEmail()); }
		 */
        storeEntity.setAddedBy("admin");

        return storeEntity;
    }

    private String getEncryptedPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    private String genereateUserId() {
        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestampStr = timestamp.format(formatter);
        return StoreConstants.RX_CONSTANT+"_"+timestampStr+"_"+OtpHandler.generateOTP(false);
    }

    public LoginUserResponse loginUser(LoginUserRequest request) throws Exception {
        LoginUserResponse response = new LoginUserResponse();

        if ((request.getEmail() == null && request.getPhoneNumber() == null) || (request.getEmail().isEmpty() && request.getPhoneNumber().isEmpty())) {
            throw new APIException("email and phone number both can't be null/empty");
        }
        TabStoreUserEntity tabStoreUserEntity = repositoryHandler.loginUser(request);
        if (null != tabStoreUserEntity) {
            response.setUserId(tabStoreUserEntity.getUserId());
            response.setUserFullName(tabStoreUserEntity.getName());
            response.setUserType(tabStoreUserEntity.getUserType());
            response.setUserEmailAddress(tabStoreUserEntity.getStoreUserEmail());
            response.setUserContact(tabStoreUserEntity.getStoreUserContact());
        }
        return response;
    }

    private StoreEntity getEntityFromUserRequest(AddUserRequest request){
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setOwner(request.getName());
        storeEntity.setOwnerContact(request.getPhoneNumber());
        storeEntity.setOwnerEmail(request.getEmail());
        storeEntity.setLocation(request.getAddress());
        storeEntity.setRole(request.getRole());
        storeEntity.setCreationTimeStamp(LocalDateTime.now().toString());

        //setting dummy parameters.
        if(request.getPhoneNumber() != null){
            storeEntity.setId("DUMMY" + request.getPhoneNumber());

        }else{
            storeEntity.setId("DUMMY" + request.getEmail());
        }
        storeEntity.setAddedBy("admin");

        return storeEntity;
    }
	public boolean verifyEmailOTP(String email, @Valid String emailOtp) {
		return repositoryHandler.verifyEmailOtp(email,emailOtp);
	}
	public boolean verifySmsOTP(@Valid OTPRequest smsOtp) {
		 return repositoryHandler.verifyPhoneOtp(smsOtp.getOtp(), smsOtp.getPhoneNumber());
	}
	public boolean sendEmailOtp(@Valid AddTabStoreUserRequest request) {
        TabStoreUserEntity entity = new TabStoreUserEntity();
		return repositoryHandler.sendEmailOtp(entity);
	}
}
