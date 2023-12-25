package com.kosuri.stores.model.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class VerifyOTPRequest {

    @NonNull
    private String otp;
    private String email;
    private String phoneNumber;
}
