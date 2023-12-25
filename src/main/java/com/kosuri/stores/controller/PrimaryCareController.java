package com.kosuri.stores.controller;
import com.kosuri.stores.exception.APIException;
import com.kosuri.stores.handler.PrimaryCareHandler;
import com.kosuri.stores.model.request.*;
import com.kosuri.stores.model.response.GenericResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class PrimaryCareController {
    @Autowired
    PrimaryCareHandler userHandler;


    @PostMapping("/addPrimaryCare")
    public ResponseEntity<GenericResponse> addPrimaryCare(@Valid @RequestBody PrimaryCareUserRequest request) {

        HttpStatus httpStatus;
        GenericResponse response = new GenericResponse();
        try {
            userHandler.addPrimaryCare(request);
            httpStatus = HttpStatus.OK;
            response.setResponseMessage("Primary Care Centre added successfully");
        }
        catch (APIException e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            response.setResponseMessage(e.getMessage());
        }
        catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            response.setResponseMessage(e.getMessage());
        }

        return ResponseEntity.status(httpStatus).body(response);
    }

    @PutMapping("/updatePrimaryCareCenter")
    public ResponseEntity<GenericResponse> updateDiagnosticCenter(@Valid @RequestBody PrimaryCareUserRequest request) {
        HttpStatus httpStatus;
        GenericResponse response = new GenericResponse();
        try {
            userHandler.updatePrimaryCareCenter(request);
            httpStatus = HttpStatus.OK;
            response.setResponseMessage("Primary Care Center updated successfully");
        } catch (APIException e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            response.setResponseMessage(e.getMessage());
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            response.setResponseMessage(e.getMessage());
        }

        return ResponseEntity.status(httpStatus).body(response);
    }
}




