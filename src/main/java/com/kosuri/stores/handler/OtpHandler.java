package com.kosuri.stores.handler;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosuri.stores.EmailVerification;
import com.kosuri.stores.dao.UserOTPEntity;
import com.kosuri.stores.dao.UserOTPRepository;
import com.kosuri.stores.model.request.AddTabStoreUserRequest;
import com.kosuri.stores.template.EmailTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class OtpHandler  {
	
	@Autowired
    private EmailServiceImpl emailService;
	
	@Autowired
    private UserOTPRepository userOtpRepository;
  

    
    public boolean sendOtpToEmail(String email, AddTabStoreUserRequest user) {
        //Generate The Template to send OTP
        Optional<UserOTPEntity> userOtpOptional = userOtpRepository.findById(email);
        String otp = String.valueOf(EmailVerification.generateOTP());
        EmailTemplate template = new EmailTemplate("static/send-otp.html");
        Map<String,String> replacements = new HashMap<>();

        replacements.put("user", email);
        replacements.put("otp", otp);
        String message = template.getTemplate(replacements);
        boolean messageSent =  emailService.sendEmailMessage(email,message,"OTP For RxWala");
       if(messageSent) {
    	   UserOTPEntity userOtp = new UserOTPEntity();
           if (userOtpOptional.isPresent()) {
               userOtp = userOtpOptional.get();
               userOtp.setEmailOtp(otp);
               userOtp.setEmailOtpDate(LocalDateTime.now().toString());
			} /*
				 * else { userOtp =
				 * UserOTPEntity.builder().emailOtpDate(LocalDate.now(ZoneOffset.UTC))
				 * .emailOtp(otp).email(email).build(); }
				 */
           userOtpRepository.save(userOtp);
       }
        return messageSent;
    }

	/*
	 * @Override public boolean sendOtpToPhoneNumber(String phoneNumber,User user) {
	 * //Generate The Template to send OTP Optional<UserOtp> userOtpOptional =
	 * userOtpRepository.findByUserAndActiveTrue(user); String otp =
	 * String.valueOf(PhoneUtil.generateFourDigitOTP()); String smsMessage =
	 * String.format(appProperties.getOtp().getSmsMessage(), otp); boolean
	 * messageSent = smsService.sendSMSMessage(phoneNumber,smsMessage);
	 * if(messageSent) { UserOtp userOtp; if (userOtpOptional.isPresent()) { userOtp
	 * = userOtpOptional.get(); userOtp.setPhoneOtp(otp);
	 * userOtp.setPhoneOtpDate(LocalDate.now(ZoneOffset.UTC));
	 * userOtp.setPhoneNumber(phoneNumber); } else { userOtp =
	 * UserOtp.builder().phoneOtpDate(LocalDate.now(ZoneOffset.UTC))
	 * .phoneOtp(otp).phoneNumber(phoneNumber).user(user).build(); }
	 * userOtpRepository.save(userOtp); } return messageSent; }
	 * 
	 * @Override public boolean validateEmailOtp(String otp, String email) {
	 * log.info("Validating Email OTP sent to Email {}",email); Optional<UserOtp>
	 * userOtpOptional = userOtpRepository.findByEmailAndActiveTrue(email);
	 * if(userOtpOptional.isPresent()){ UserOtp userOtp = userOtpOptional.get();
	 * return userOtp.getEmailOtp().equals(otp); } return false; }
	 * 
	 * @Override public boolean validatePhoneOtp(String otp, String mobileNumber) {
	 * log.info("Validating Email OTP sent to MobileNumber {}",mobileNumber);
	 * Optional<UserOtp> userOtpOptional =
	 * userOtpRepository.findByPhoneNumberAndActiveTrue(mobileNumber);
	 * if(userOtpOptional.isPresent()){ UserOtp userOtp = userOtpOptional.get();
	 * return userOtp.getPhoneOtp().equals(otp); } return false; }
	 */

}
