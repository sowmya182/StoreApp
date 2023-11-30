package com.kosuri.stores.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosuri.stores.SendSMS;
import com.kosuri.stores.exception.APIException;
import com.kosuri.stores.handler.UserHandler;
import com.kosuri.stores.model.request.AddTabStoreUserRequest;
import com.kosuri.stores.model.request.AddUserRequest;
import com.kosuri.stores.model.request.LoginUserRequest;
import com.kosuri.stores.model.response.GenericResponse;
import com.kosuri.stores.model.response.LoginUserResponse;
import com.kosuri.stores.model.response.OTPResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserHandler userHandler;

	@Autowired
	private SendSMS sendSMS;

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
		} catch (Exception e) {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			response.setResponseMessage(e.getMessage());
		}
		return ResponseEntity.status(httpStatus).body(response);
	}

	@PostMapping("/addstoreuser")
	public ResponseEntity<GenericResponse> addUser(@Valid @RequestBody AddTabStoreUserRequest request) {
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

	@PostMapping("/sendEmailOtp")
	public ResponseEntity<OTPResponse> sendEmailOTP(@Valid AddTabStoreUserRequest request) {
		HttpStatus httpStatus;
		OTPResponse response = new OTPResponse();
		try {
			String emailotp = userHandler.sendEmailOtp(request);
			httpStatus = HttpStatus.OK;
			response.setOtp(emailotp);
		} catch (Exception e) {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			response.setOtp(e.getMessage());
		}
		return ResponseEntity.status(httpStatus).body(response);
	}

	@PostMapping("/sendSMSOtp")
	public String sendSMSOTP(@Valid String phoneNumber) {
		String otp = null;
		try {
			otp = sendSMS.sendSms(phoneNumber);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return otp;
	}

	@PostMapping("/verifyEmailOTP")
	public ResponseEntity<GenericResponse> verifyEmailOTP(@Valid @RequestParam("EmailOTP") String emailOtp) {
		HttpStatus httpStatus;
		GenericResponse response = new GenericResponse();
		try {
			boolean isEmailVerified = userHandler.verifyEmailOTP(emailOtp);
			httpStatus = HttpStatus.OK;
			if (isEmailVerified) {
				response.setResponseMessage("Email Verification Success");
			}
		} catch (Exception e) {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			response.setResponseMessage(e.getMessage());
		}

		return ResponseEntity.status(httpStatus).body(response);
	}

	@PostMapping("/verifySmsOTP")
	public ResponseEntity<GenericResponse> verifySMSOTP(@Valid @RequestParam("SMSOTP") String smsOtp) {
		HttpStatus httpStatus;
		GenericResponse response = new GenericResponse();
		try {
			boolean isEmailVerified = userHandler.verifySmsOTP(smsOtp);
			httpStatus = HttpStatus.OK;
			if (isEmailVerified) {
				response.setResponseMessage("SMS Verification Success");
			}
		} catch (Exception e) {
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			response.setResponseMessage(e.getMessage());
		}

		return ResponseEntity.status(httpStatus).body(response);
	}
}
