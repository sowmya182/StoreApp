package com.kosuri.stores.handler;

import com.kosuri.stores.dao.StoreEntity;
import com.kosuri.stores.dao.StoreRepository;
import com.kosuri.stores.exception.APIException;
import com.kosuri.stores.model.request.CreateStoreRequest;
import com.kosuri.stores.model.request.UpdateStoreRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StoreHandler {
    @Autowired
    private RepositoryHandler repositoryHandler;

    @Autowired
    private StoreRepository storeRepository;

    public String addStore(CreateStoreRequest createStoreRequest) throws Exception{
        if(validateStoreInputs(createStoreRequest)) {
            StoreEntity storeEntity = repositoryHandler.addStoreToRepository(createStoreEntityFromRequest(createStoreRequest));
        }
        return createStoreRequest.getId();
    }

    public String updateStore(UpdateStoreRequest updateStoreRequest) throws Exception {
        if(validateUpdateStoreInputs(updateStoreRequest)) {
            StoreEntity storeEntity = repositoryHandler.updateStore(updateStoreEntityFromRequest(updateStoreRequest));
        }
        return updateStoreRequest.getId();
    }

    public String getStoreIdFromStoreOwner(String emailId) {
        Optional<List<StoreEntity>> entity = storeRepository.findByOwnerEmail(emailId);
        if (entity.isPresent()) {
            for (StoreEntity store: entity.get()) {
                if (store.getId().contains("DUMMY")) {
                    continue;
                }
                return store.getId();
            }
        }
        return "";
    }

    public List<StoreEntity> getAllStores() throws Exception{
        List<StoreEntity> storeEntities = repositoryHandler.getAllStores();

        List<StoreEntity> stores = new ArrayList<>();
        for(StoreEntity store: storeEntities){
            if(store.getId() != "DUMMY"){
                stores.add(store);
            }
        }
        return stores;
    }

    private StoreEntity createStoreEntityFromRequest(CreateStoreRequest createStoreRequest){
        //TODO add location and other fields from request instead of default values.
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setName(createStoreRequest.getName());
        storeEntity.setId(createStoreRequest.getId());
        storeEntity.setType(createStoreRequest.getStoreType());
        storeEntity.setPincode(createStoreRequest.getPincode());
        storeEntity.setDistrict(createStoreRequest.getDistrict());
        storeEntity.setState(createStoreRequest.getState());
        storeEntity.setOwner(createStoreRequest.getOwner());
        storeEntity.setOwnerEmail(createStoreRequest.getOwnerEmail());
        storeEntity.setOwnerContact(createStoreRequest.getOwnerContact());
        storeEntity.setSecondaryContact(createStoreRequest.getSecondaryContact());
        storeEntity.setRegistrationDate(LocalDate.now().toString());
        storeEntity.setCreationTimeStamp(LocalDateTime.now().toString());
//        storeEntity.setRole("test");
        storeEntity.setModifiedBy("test_user");
        storeEntity.setModifiedDate(LocalDate.now().toString());
        storeEntity.setModifiedTimeStamp(LocalDateTime.now().toString());
        storeEntity.setStatus("Active");
//        storeEntity.setPassword("test");
        storeEntity.setAddedBy(createStoreRequest.getOwner());
        storeEntity.setLocation(createStoreRequest.getLocation());

        return storeEntity;
    }

    private StoreEntity updateStoreEntityFromRequest(UpdateStoreRequest request){
        //TODO add location and other fields from request instead of default values.
        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setName(request.getName());
        storeEntity.setId(request.getId());
        storeEntity.setType(request.getStoreType());
        storeEntity.setPincode(request.getPincode());
        storeEntity.setPincode(request.getPincode());
        storeEntity.setDistrict(request.getDistrict());
        storeEntity.setState(request.getState());
        storeEntity.setOwner(request.getOwner());
        storeEntity.setOwnerEmail(request.getOwnerEmail());
        storeEntity.setOwnerContact(request.getOwnerContact());
        storeEntity.setSecondaryContact(request.getSecondaryContact());
        storeEntity.setRegistrationDate(LocalDate.now().toString());
        storeEntity.setCreationTimeStamp(LocalDateTime.now().toString());
        storeEntity.setRole("test");
        storeEntity.setModifiedBy("test_user");
        storeEntity.setModifiedDate(LocalDate.now().toString());
        storeEntity.setModifiedTimeStamp(LocalDateTime.now().toString());
        storeEntity.setStatus(request.getStatus());
        storeEntity.setPassword("test");
        storeEntity.setAddedBy(request.getOwner());
        storeEntity.setLocation(request.getLocation());

        return storeEntity;
    }

    boolean validateStoreInputs(CreateStoreRequest request) throws Exception{
        Optional<StoreEntity> store = storeRepository.findById(request.getId());
        if(store.isPresent()){
            throw new APIException("Store with id is already present in system");
        }

        if(request.getOwnerEmail() != null && !request.getOwnerEmail().isEmpty() && request.getOwnerContact() != null && !request.getOwnerContact().isEmpty()){
            Optional<List<StoreEntity>> store2 = storeRepository.findByOwnerEmailOrOwnerContact(request.getOwnerEmail(), request.getOwnerContact());
            if(!store2.get().isEmpty()){
                for(StoreEntity s: store2.get()){
                    if(!s.getId().contains("DUMMY")){
                        throw new APIException("Store with owner email/contact is already present in system");
                    }
                }
            }
            boolean isUserPresent = false;
            if(!store2.get().isEmpty()){
                for(StoreEntity s: store2.get()){
                    if(s.getId().contains("DUMMY") && s.getRole().equals("STORE_MANAGER")){
                        isUserPresent = true;
                    }
                }
            }

            if(!isUserPresent){
                throw new APIException("Store owner not present as user in system");

            }
        }
        return true;
    }

    boolean validateUpdateStoreInputs(UpdateStoreRequest request) throws Exception{
        Optional<StoreEntity> store = storeRepository.findById(request.getId());
        if(!store.isPresent()){
            throw new APIException("Store with id not found");
        }

        if(request.getOwnerEmail() != null && !request.getOwnerEmail().isEmpty() && request.getOwnerContact() != null && !request.getOwnerContact().isEmpty()){
        Optional<List<StoreEntity>> store2 = storeRepository.findByOwnerEmailOrOwnerContact(request.getOwnerEmail(), request.getOwnerContact());
            if(!store2.get().isEmpty()){
                for(StoreEntity s: store2.get()){
                    if(!s.getId().contains("DUMMY") && s.getId() != request.getId()){
                        throw new APIException("Store with owner email/contact is already present in system");
                    }
                }
            }
            boolean isUserPresent = false;
            if(!store2.get().isEmpty()){
                for(StoreEntity s: store2.get()){
                    if(s.getId().contains("DUMMY") && s.getRole().equals("STORE_MANAGER")){
                        isUserPresent = true;
                    }
                }
            }

            if(!isUserPresent){
                throw new APIException("Store owner not present as user in system");

            }
        }
        return true;
    }
}
