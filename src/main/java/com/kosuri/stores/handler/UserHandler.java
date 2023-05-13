package com.kosuri.stores.handler;

import com.kosuri.stores.dao.StoreEntity;
import com.kosuri.stores.exception.APIException;
import com.kosuri.stores.model.request.AddUserRequest;
import com.kosuri.stores.model.request.LoginUserRequest;
import com.kosuri.stores.model.response.LoginUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public LoginUserResponse loginUser(LoginUserRequest request) throws Exception {
        LoginUserResponse response = new LoginUserResponse();

        if ((request.getEmail() == null && request.getPhoneNumber() == null) || (request.getEmail().isEmpty() && request.getPhoneNumber().isEmpty())) {
            throw new APIException("email and phone number both can't be null/empty");
        }

        StoreEntity storeEntity = repositoryHandler.loginUser(request);
        List<String> stores = new ArrayList<>();
        if(storeEntity.getRole().equals("STORE_MANAGER")){
            String storeId = storeHandler.getStoreIdFromStoreOwner(request.getEmail());
            String roleId = roleHandler.getRoleIdFromRoleName(storeEntity.getRole());
            stores.add(storeId);
            response.setRoleName(storeEntity.getRole());
            response.setRoleId(roleId);
            response.setStoreId(stores);
        } else {
            List<String> storeIds = storeHandler.getStoreIdFromLocation(storeEntity.getLocation());
            String roleId = roleHandler.getRoleIdFromRoleName(storeEntity.getRole());
            stores.addAll(storeIds);
            response.setRoleName(storeEntity.getRole());
            response.setRoleId(roleId);
            response.setStoreId(stores);
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
        storeEntity.setPassword(request.getPassword());
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
}
