package com.kosuri.stores.handler;

import com.kosuri.stores.dao.*;
import com.kosuri.stores.exception.APIException;
import com.kosuri.stores.model.enums.Role;
import com.kosuri.stores.model.request.AddUserRequest;
import com.kosuri.stores.model.request.LoginUserRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepositoryHandler {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StockRepository stockRepository;

    public StoreEntity addStoreToRepository(@Valid StoreEntity storeEntity) throws Exception {
        Optional<StoreEntity> store = storeRepository.findById(storeEntity.getId());
        boolean storeExistsByOwnerEmail = storeRepository.existsByOwnerEmail(storeEntity.getOwnerEmail());
        if (store.isPresent()) {
            throw new APIException("Store with this id is already present");
        }
        return storeRepository.save(storeEntity);

    }

    public StoreEntity updateStore(@Valid StoreEntity storeEntity) throws Exception {
        Optional<StoreEntity> store = storeRepository.findById(storeEntity.getId());

        if(store.isEmpty()){
            System.out.println("Entity not found");
            throw new APIException("Store with this id not found!");
        }

        return storeRepository.save(storeEntity);
    }

    public void addUser(@Valid StoreEntity storeEntity, AddUserRequest request) throws Exception {
        //TODO Update to query based on id
        Optional<RoleEntity> role = roleRepository.findByRoleName(request.getRole());
        if(!role.isPresent()){
            throw new APIException("Role does not exist. Please enter a valid role");
        }

        storeRepository.save(storeEntity);

    }

    public List<StoreEntity> getAllStores() throws Exception {
        List<StoreEntity> storeEntities = storeRepository.findAll();
        return storeEntities;
    }


    public Optional<List<PurchaseEntity>> getPurchaseRecordsByStore(String storeId){
        return purchaseRepository.findByStoreId(storeId);
    }

    public Optional<List<SaleEntity>> getSaleRecordsByStore(String storeId){
        return saleRepository.findByStoreId(storeId);
    }

    public Optional<List<StockEntity>> getStockRecordsByStore(String storeId) {
        return stockRepository.findByStoreId(storeId);
    }

    public boolean validateuser(AddUserRequest request) throws Exception{
        Optional<List<StoreEntity>> existingStores = storeRepository.findByOwnerEmailOrOwnerContact(request.getEmail(), request.getPhoneNumber());
        if(existingStores.isEmpty()){
            return true;
        }
        for (StoreEntity store: existingStores.get()){
            //TODO Update to query based on id
            if (store.getId().contains("DUMMY")){
                System.out.println("User already exists in system");
                throw new APIException("User already exists in system");
            }
        }

        return true;
    }

    public StoreEntity loginUser(LoginUserRequest request) throws Exception {
        Optional<List<StoreEntity>> existingStores = storeRepository.findByOwnerEmailOrOwnerContact(request.getEmail(), request.getPhoneNumber());
        if (existingStores.isEmpty()) {
            throw new APIException("Invalid Credentials!");
        }

        for (StoreEntity store : existingStores.get()) {
            //TODO Update to query based on id
            if (store.getPassword() != null && store.getPassword().equals(request.getPassword()) && store.getId().contains("DUMMY")) {
                System.out.println("User logged in successfully");
                return store;
            }
        }

        throw new APIException("Invalid Credentials!");
    }

}
