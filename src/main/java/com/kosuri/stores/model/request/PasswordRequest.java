package com.kosuri.stores.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRequest {

    private String emailAddress;
    private String userContactNumber;
    private String password;
    private String confirmPassword;
    private String otp;
    private Boolean isForgetPassword;

}
