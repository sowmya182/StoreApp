package com.kosuri.stores.handler;


import com.kosuri.stores.dao.DiagnosticServicesEntity;
import com.kosuri.stores.model.request.DiagnosticCenterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DiagnosticHandler {

    @Autowired
    private RepositoryHandler repositoryHandler;

    public boolean addDiagnosticCenter(DiagnosticCenterRequest request) throws Exception {
        boolean isDCActive = false;
        if (!repositoryHandler.isDCActive(request)){
            return false;
        }

        DiagnosticServicesEntity diagnosticServicesEntity = getDiagnosticServicesEntityFromDiagnosticRequest(request, isDCActive);

        try {
            repositoryHandler.addDiagnosticCenter(diagnosticServicesEntity,request);
        } catch (DataIntegrityViolationException e) {
            throw new Exception(e.getCause().getCause().getMessage());
        }
        return true;
    }

    private DiagnosticServicesEntity getDiagnosticServicesEntityFromDiagnosticRequest(DiagnosticCenterRequest request, boolean isDCActive) {

        DiagnosticServicesEntity diagnosticServicesEntity = new DiagnosticServicesEntity();

        diagnosticServicesEntity.setServiceCategory(request.getServiceCategory());
        diagnosticServicesEntity.setServiceName(request.getServiceName());
        diagnosticServicesEntity.setPrice(request.getPrice());
        diagnosticServicesEntity.setUserId(request.getUserId());
        diagnosticServicesEntity.setDescription(request.getDescription());
        diagnosticServicesEntity.setUpdatedBy(request.getUserId());
        diagnosticServicesEntity.setServiceId(request.getServiceId());
        diagnosticServicesEntity.setUserServiceId(request.getUserId()+"_"+request.getServiceId());
        diagnosticServicesEntity.setStatus(isDCActive?"1":"0");
        diagnosticServicesEntity.setStatusUpdatedDate(LocalDateTime.now().toString());
        diagnosticServicesEntity.setAmountUpdatedDate((LocalDateTime.now().toString()));

        return diagnosticServicesEntity;
    }

    public boolean updateDiagnosticCenter(DiagnosticCenterRequest request) throws Exception{

        String userServiceId = request.getUserId()+"_"+request.getServiceId();

        // Retrieve the Diagnostic Services Entity
        DiagnosticServicesEntity serviceEntity = repositoryHandler.findServiceById(userServiceId);

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

        if (!repositoryHandler.isDCActive(request)) {
            serviceEntity.setStatus(repositoryHandler.isDCActive(request)?"1":"0");
            serviceEntity.setStatusUpdatedDate(LocalDateTime.now().toString());
            isUpdated = true;
        }

        // Save the updated entity
        if (isUpdated) {
            repositoryHandler.saveDiagnosticServiceEntity(serviceEntity);
        }

        return isUpdated;
    }
}
