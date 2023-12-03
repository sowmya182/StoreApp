package com.kosuri.stores.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class OTPRequest {

    @NonNull
    private String otp;
    private String email;
    private String phoneNumber;
}
