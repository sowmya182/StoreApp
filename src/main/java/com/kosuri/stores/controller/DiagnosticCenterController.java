package com.kosuri.stores.controller;

import com.kosuri.stores.exception.APIException;
import com.kosuri.stores.handler.DiagnosticHandler;
import com.kosuri.stores.model.response.GenericResponse;
import com.kosuri.stores.model.request.DiagnosticCenterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diagnosticCenter")
public class DiagnosticCenterController {

    @Autowired
    private DiagnosticHandler diagnosticHandler;

    @PostMapping("/addDiagnosticCenter")
    public ResponseEntity<GenericResponse> addUser(@Valid @RequestBody DiagnosticCenterRequest request) {
        HttpStatus httpStatus;
        GenericResponse response = new GenericResponse();
        try {
            diagnosticHandler.addDiagnosticCenter(request);
            httpStatus = HttpStatus.OK;
            response.setResponseMessage("Diagnostic Center added successfully");
        } catch (APIException e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            response.setResponseMessage(e.getMessage());
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            response.setResponseMessage(e.getMessage());
        }

        return ResponseEntity.status(httpStatus).body(response);
    }

    @PutMapping("/updateDiagnosticCenter")
    public ResponseEntity<GenericResponse> updateDiagnosticCenter(@Valid @RequestBody DiagnosticCenterRequest request) {
        HttpStatus httpStatus;
        GenericResponse response = new GenericResponse();
        try {
            diagnosticHandler.updateDiagnosticCenter(request);
            httpStatus = HttpStatus.OK;
            response.setResponseMessage("Diagnostic Center updated successfully");
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
