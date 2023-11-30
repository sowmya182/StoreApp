package com.kosuri.stores.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_otp")
public class UserOTPEntity {
	@Id
	@NotNull
	private @Column(name = "user_otp_id") String userOtpId;
	private @Column(name = "active") Integer active;
	private @Column(name = "created_on") String createdOn;
	private @Column(name = "updated_on") String updatedOn;
	private @Column(name = "email") String userEmail;
	private @Column(name = "email_otp_date") String emailOtpDate;
	private @Column(name = "phone_number") String userPhoneNumber;
	private @Column(name = "phone_otp_date") String phoneOtpDate;
	private @Column(name = "email_otp") String emailOtp;
	private @Column(name = "phone_otp") String phoneOtp;
	private @Column(name = "email_verify") boolean emailVerify;
	private @Column(name = "sms_verify") boolean smsVerify;
	public String getUserOtpId() {
		return userOtpId;
	}
	public void setUserOtpId(String userOtpId) {
		this.userOtpId = userOtpId;
	}
	public Integer getActive() {
		return active;
	}
	public void setActive(Integer active) {
		this.active = active;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getEmailOtpDate() {
		return emailOtpDate;
	}
	public void setEmailOtpDate(String emailOtpDate) {
		this.emailOtpDate = emailOtpDate;
	}
	public String getUserPhoneNumber() {
		return userPhoneNumber;
	}
	public void setUserPhoneNumber(String userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}
	public String getPhoneOtpDate() {
		return phoneOtpDate;
	}
	public void setPhoneOtpDate(String phoneOtpDate) {
		this.phoneOtpDate = phoneOtpDate;
	}
	public String getEmailOtp() {
		return emailOtp;
	}
	public void setEmailOtp(String emailOtp) {
		this.emailOtp = emailOtp;
	}
	public String getPhoneOtp() {
		return phoneOtp;
	}
	public void setPhoneOtp(String phoneOtp) {
		this.phoneOtp = phoneOtp;
	}
	public boolean isEmailVerify() {
		return emailVerify;
	}
	public void setEmailVerify(boolean emailVerify) {
		this.emailVerify = emailVerify;
	}
	public boolean isSmsVerify() {
		return smsVerify;
	}
	public void setSmsVerify(boolean smsVerify) {
		this.smsVerify = smsVerify;
	}
	
	
}
