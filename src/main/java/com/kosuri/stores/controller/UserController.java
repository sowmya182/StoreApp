package com.kosuri.stores.controller;

import com.kosuri.stores.exception.APIException;
import com.kosuri.stores.handler.UserHandler;
import com.kosuri.stores.model.request.AddUserRequest;
import com.kosuri.stores.model.request.LoginUserRequest;
import com.kosuri.stores.model.response.GenericResponse;
import com.kosuri.stores.model.response.LoginUserResponse;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserHandler userHandler;
    @PostMapping("/add")
    public ResponseEntity<GenericResponse> addUser(@Valid @RequestBody AddUserRequest request) {
        HttpStatus httpStatus;
        GenericResponse response = new GenericResponse();
        try {
            userHandler.addUser(request);
            httpStatus = HttpStatus.OK;
            response.setResponseMessage("User added successfully");
        } catch (APIException e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            response.setResponseMessage(e.getMessage());
        } catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            response.setResponseMessage(e.getMessage());
        }

        return ResponseEntity.status(httpStatus).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@Valid @RequestBody LoginUserRequest request) {
        HttpStatus httpStatus;
        LoginUserResponse response = new LoginUserResponse();
        String body;
        try {
            response = userHandler.loginUser(request);
            httpStatus = HttpStatus.OK;
            response.setResponseMessage("User logged in successfully!");
        } catch (APIException e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            response.setResponseMessage(e.getMessage());
        }
        catch (Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            response.setResponseMessage(e.getMessage());
        }
        return ResponseEntity.status(httpStatus).body(response);
    }
}
