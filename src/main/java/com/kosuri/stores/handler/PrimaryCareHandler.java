package com.kosuri.stores.handler;

import com.kosuri.stores.dao.PrimaryCareCenterRepository;
import com.kosuri.stores.dao.PrimaryCareEntity;
import com.kosuri.stores.model.request.PrimaryCareUserRequest;
import com.kosuri.stores.model.response.GetAllPrimaryCareCentersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrimaryCareHandler {

@Autowired
private RepositoryHandler  repositoryHandler;

@Autowired
private PrimaryCareCenterRepository primaryCareCenterRepository;
        public boolean addPrimaryCare(PrimaryCareUserRequest request) throws Exception {
        if(!repositoryHandler.isPCActive(request)) {
            return false;
        }

        PrimaryCareEntity primaryCareEntity = setEntityFromPrimaryCareRequest(request);
        boolean isPCAdded = false;
            try {
                isPCAdded =  repositoryHandler.addPrimaryCareCenter(primaryCareEntity, request);
            } catch (DataIntegrityViolationException e) {
                throw new Exception(e.getCause().getCause().getMessage());
            }
            return isPCAdded;

    }

    private PrimaryCareEntity setEntityFromPrimaryCareRequest(PrimaryCareUserRequest request) {
        PrimaryCareEntity primaryEntity = new PrimaryCareEntity();
        primaryEntity.setServiceId(request.getServiceId());
        primaryEntity.setUserId(request.getUserId());
        primaryEntity.setServiceName(request.getServiceName());
        primaryEntity.setServiceCategory(request.getServiceCategory());
        primaryEntity.setDescription(request.getDescription());
        primaryEntity.setUpdatedBy(request.getUpdatedBy());
        primaryEntity.setPrice(request.getPrice());
        return primaryEntity;
    }
    public boolean updatePrimaryCareCenter(PrimaryCareUserRequest request) throws Exception{

        String userServiceId = request.getUserId()+"_"+request.getServiceId();

        // Retrieve the Diagnostic Services Entity
      PrimaryCareEntity serviceEntity= repositoryHandler.findPrimaryServiceById(userServiceId);

        if (serviceEntity == null) {
            throw new Exception("Store not found");
        }

        // Check and update the price, status, and timestamps
        boolean isUpdated = false;
        serviceEntity.setServiceCategory(request.getServiceCategory());
        serviceEntity.setServiceName(request.getServiceName());
        serviceEntity.setDescription(request.getDescription());
        serviceEntity.setUserId(request.getUserId());
        serviceEntity.setUpdatedBy(request.getUserId());
        serviceEntity.setServiceId(request.getServiceId());

        if (request.getPrice() != null && !request.getPrice().equals(serviceEntity.getPrice())) {
            serviceEntity.setPrice(request.getPrice());
            serviceEntity.setAmountUpdatedDate((LocalDateTime.now().toString()));
            isUpdated = true;
        }

        if (!repositoryHandler.isPCActive(request)) {
            serviceEntity.setStatus(repositoryHandler.isPCActive(request)?"1":"0");
            serviceEntity.setStatusUpdatedDate(LocalDateTime.now().toString());
            isUpdated = true;
        }

        // Save the updated entity
        if (isUpdated) {
            repositoryHandler.savePrimaryServiceEntity(serviceEntity);
        }

        return isUpdated;
    }

    public GetAllPrimaryCareCentersResponse getAllPrimaryCareCenters() {
        GetAllPrimaryCareCentersResponse response = new GetAllPrimaryCareCentersResponse();
        List<PrimaryCareEntity> primaryCareEntities = new ArrayList<>();
        primaryCareCenterRepository.findAll().forEach(primaryCareCenter -> primaryCareEntities.add(primaryCareCenter));
        response.setPrimaryCareCenters(primaryCareEntities);
        return response;

    }

    public GetAllPrimaryCareCentersResponse getPrimaryCareCenterByLocationOrUserId(String location, String userId) {
            List<PrimaryCareEntity> primaryCareCenters = primaryCareCenterRepository.
                    findByUserId(userId);
            GetAllPrimaryCareCentersResponse response = new GetAllPrimaryCareCentersResponse();
            response.setPrimaryCareCenters(primaryCareCenters);
            return response;

    }
}


