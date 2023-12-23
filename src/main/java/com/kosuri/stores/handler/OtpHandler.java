package com.kosuri.stores.handler;


import com.kosuri.stores.dao.TabStoreUserEntity;
import com.kosuri.stores.dao.UserOTPEntity;
import com.kosuri.stores.dao.UserOTPRepository;
import com.kosuri.stores.model.request.AddTabStoreUserRequest;
import com.kosuri.stores.template.EmailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class OtpHandler {


    private final EmailService emailService;

    private final SmsHandler smsHandler;
    @Autowired
    public OtpHandler(EmailService emailService, SmsHandler smsHandler) {
        this.emailService = emailService;
        this.smsHandler = smsHandler;
    }

    @Autowired
    private UserOTPRepository userOtpRepository;


    public boolean sendOtpToEmail(String email) {
        //Generate The Template to send OTP
        Optional<UserOTPEntity> userOtpOptional = userOtpRepository.findByUserEmail(email);
        String otp = OtpHandler.generateOTP(true);
        EmailTemplate template = new EmailTemplate("static/send-otp.html");
        Map<String, String> replacements = new HashMap<>();

        replacements.put("user", email);
        replacements.put("otp", otp);
        String message = template.getTemplate(replacements);
        boolean messageSent = emailService.sendEmailMessage(email, message, "OTP For RxWala");
        if (messageSent) {
            UserOTPEntity userOtp = new UserOTPEntity();
            if (userOtpOptional.isPresent()) {
                userOtp = userOtpOptional.get();
                userOtp.setEmailOtp(otp);
                userOtp.setEmailOtpDate(new Date());
            }
            userOtpRepository.save(userOtp);
        }
        return messageSent;
    }

    public boolean sendOtpToPhoneNumber(String phoneNumber) {

        Optional<UserOTPEntity> userOtpOptional = userOtpRepository.findByUserPhoneNumber(phoneNumber);
        String otp = OtpHandler.generateOTP(true);
        boolean messageSent = smsHandler.sendSMSMessage(phoneNumber,otp);
        if (messageSent) {
            if (userOtpOptional.isPresent()) {
                UserOTPEntity userOtp = userOtpOptional.get();
                userOtp.setPhoneOtp(otp);
                userOtp.setPhoneOtpDate(new Date());
                userOtpRepository.save(userOtp);
            }
        }
        return messageSent;
    }


    public static String generateOTP(boolean isOTP) {
        Random random = new Random();
        int lowerBound = isOTP ? 100000 : 1000;
        return String.valueOf(lowerBound + random.nextInt(999999 - lowerBound + 1));
    }


}
