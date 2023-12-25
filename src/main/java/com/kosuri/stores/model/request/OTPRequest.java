package com.kosuri.stores.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OTPRequest {

    private String email;
    private String phoneNumber;
    private Boolean isForgetPassword;
}
